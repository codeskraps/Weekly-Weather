package com.codeskraps.feature.geocoding.presentation

import androidx.lifecycle.viewModelScope
import com.codeskraps.feature.common.dispatcher.DispatcherProvider
import com.codeskraps.core.local.domain.model.GeoLocation
import com.codeskraps.core.local.domain.repository.LocalGeocodingRepository
import com.codeskraps.core.local.domain.repository.LocalResourceRepository
import com.codeskraps.feature.common.mvi.StateReducerViewModel
import com.codeskraps.feature.common.util.Resource
import com.codeskraps.feature.geocoding.repository.GeocodingRepository
import com.codeskraps.feature.geocoding.presentation.mvi.GeoAction
import com.codeskraps.feature.geocoding.presentation.mvi.GeoEvent
import com.codeskraps.feature.geocoding.presentation.mvi.GeoState
import com.codeskraps.umami.domain.AnalyticsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GeocodingViewModel(
    private val localGeocodingRepository: LocalGeocodingRepository,
    private val geocodingRepository: GeocodingRepository,
    private val localResources: LocalResourceRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val analyticsRepository: AnalyticsRepository
) : StateReducerViewModel<GeoState, GeoEvent, GeoAction>(GeoState.initial) {

    private companion object {
        private const val ANALYTICS_GEO_SEARCH = "geocoding_search"
        private const val ANALYTICS_GEO_SAVE = "geocoding_save"
        private const val ANALYTICS_GEO_DELETE = "geocoding_delete"
        private const val ANALYTICS_GEO_ERROR = "geocoding_error"
        private const val ANALYTICS_GEO_CACHE_LOADED = "geocoding_cache_loaded"
        
        private const val PARAM_QUERY = "query"
        private const val PARAM_RESULTS = "results_count"
        private const val PARAM_LOCATION = "location"
        private const val PARAM_ERROR = "error_message"
        private const val PARAM_LOCATIONS_COUNT = "locations_count"
    }

    private var searchJob: Job? = null

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            analyticsRepository.trackPageView("geocoding")
        }
    }

    override fun reduceState(
        currentState: GeoState,
        event: GeoEvent
    ): GeoState {
        return when (event) {
            is GeoEvent.Search -> onSearchGeoLocation(currentState, event.query)
            is GeoEvent.Delete -> onDeleteGeoLocation(currentState, event.geoLocation)
            is GeoEvent.Save -> onSaveGeoLocation(currentState, event.geoLocation)
            is GeoEvent.Loaded -> onGeoLocationsLoaded(currentState, event.geoLocations)
            is GeoEvent.Error -> onHandleError(currentState, event.message)
            is GeoEvent.LoadCache -> onLoadCache(currentState)
        }
    }

    private fun onLoadCache(currentState: GeoState): GeoState {
        viewModelScope.launch(dispatcherProvider.io) {
            when (val result = localGeocodingRepository.getCachedGeoLocation()) {
                is Resource.Success -> {
                    if (result.data.isNotEmpty()) {
                        val cachedGeoLocations = result.data.sortedBy { it.name }
                        state.handleEvent(GeoEvent.Loaded(cachedGeoLocations))
                        
                        analyticsRepository.trackEvent(
                            ANALYTICS_GEO_CACHE_LOADED,
                            mapOf(PARAM_LOCATIONS_COUNT to cachedGeoLocations.size.toString())
                        )
                    } else {
                        state.handleEvent(GeoEvent.Error(localResources.getNoResultString()))
                    }
                }

                is Resource.Error -> {
                    state.handleEvent(GeoEvent.Error(localResources.getNoResultString()))
                    actionChannel.send(GeoAction.ShowToast(localResources.getIssueLoadingCache()))
                }
            }
        }
        return currentState.copy(
            isLoading = true,
            error = null,
            geoLocations = emptyList()
        )
    }

    private suspend fun loadCache(): List<GeoLocation> {
        return when (val result = localGeocodingRepository.getCachedGeoLocation()) {
            is Resource.Success -> {
                result.data.ifEmpty {
                    emptyList()
                }
            }

            is Resource.Error -> emptyList()
        }
    }

    private fun onGeoLocationsLoaded(
        currentState: GeoState,
        geoLocations: List<GeoLocation>
    ): GeoState {
        return currentState.copy(
            isLoading = false,
            geoLocations = geoLocations,
            error = null
        )
    }

    private fun onSearchGeoLocation(currentState: GeoState, query: String): GeoState {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(dispatcherProvider.io) {
            delay(500L)

            when (val result = geocodingRepository.getGeoLocation(query)) {
                is Resource.Success -> {
                    if (result.data.isNotEmpty()) {
                        val mappedLocations = result.data.map { mapped ->
                            val cachedGeoLocations = loadCache()
                            val found = cachedGeoLocations.firstOrNull {
                                it.longitude == mapped.longitude && it.latitude == mapped.latitude
                            }
                            mapped.copy(cached = found != null)
                        }
                        
                        analyticsRepository.trackEvent(
                            ANALYTICS_GEO_SEARCH,
                            mapOf(
                                PARAM_QUERY to query,
                                PARAM_RESULTS to mappedLocations.size.toString()
                            )
                        )
                        
                        state.handleEvent(GeoEvent.Loaded(mappedLocations))
                    } else {
                        state.handleEvent(GeoEvent.Error(localResources.getNoResultString()))
                    }
                }

                is Resource.Error -> {
                    state.handleEvent(GeoEvent.Error(result.message))
                }
            }
        }

        return currentState.copy(
            isLoading = true,
            error = null,
            query = query,
            geoLocations = emptyList()
        )
    }

    private fun onSaveGeoLocation(currentState: GeoState, geoLocation: GeoLocation): GeoState {
        viewModelScope.launch(dispatcherProvider.io) {
            analyticsRepository.trackEvent(
                ANALYTICS_GEO_SAVE,
                mapOf(PARAM_LOCATION to geoLocation.name)
            )
            
            val geoLocations = state.value.geoLocations
            when (localGeocodingRepository.saveCacheGeoLocation(geoLocation)) {
                is Resource.Success -> {
                    state.handleEvent(GeoEvent.Loaded(geoLocations.map {
                        if (geoLocation.latitude == it.latitude && geoLocation.longitude == it.longitude) {
                            it.copy(cached = true)
                        } else it
                    }))
                }

                is Resource.Error -> {
                    actionChannel.send(GeoAction.ShowToast(localResources.getIssueSaving()))
                }
            }
        }
        return currentState
    }

    private fun onDeleteGeoLocation(currentState: GeoState, geoLocation: GeoLocation): GeoState {
        viewModelScope.launch(dispatcherProvider.io) {
            analyticsRepository.trackEvent(
                ANALYTICS_GEO_DELETE,
                mapOf(PARAM_LOCATION to geoLocation.name)
            )
            
            when (localGeocodingRepository.deleteCacheGeoLocation(geoLocation)) {
                is Resource.Success -> {
                    val geoLocations = state.value.geoLocations
                    state.handleEvent(GeoEvent.Loaded(geoLocations.minus(geoLocation)))
                }

                is Resource.Error -> {
                    actionChannel.send(GeoAction.ShowToast(localResources.getIssueDeleting()))
                }
            }
        }
        return currentState
    }

    private fun onHandleError(currentState: GeoState, message: String): GeoState {
        viewModelScope.launch(dispatcherProvider.io) {
            analyticsRepository.trackEvent(
                ANALYTICS_GEO_ERROR,
                mapOf(PARAM_ERROR to message)
            )
        }
        return currentState.copy(
            isLoading = false,
            error = message,
            geoLocations = emptyList()
        )
    }
}