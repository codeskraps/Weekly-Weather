package com.trifork.feature.geocoding.presentation

import androidx.lifecycle.viewModelScope
import com.trifork.feature.common.dispatcher.DispatcherProvider
import com.trifork.feature.common.domain.model.GeoLocation
import com.trifork.feature.common.domain.repository.LocalGeocodingRepository
import com.trifork.feature.common.mvi.StateReducerViewModel
import com.trifork.feature.common.util.Resource
import com.trifork.feature.geocoding.domain.repository.GeocodingRepository
import com.trifork.feature.geocoding.presentation.mvi.GeoAction
import com.trifork.feature.geocoding.presentation.mvi.GeoEvent
import com.trifork.feature.geocoding.presentation.mvi.GeoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeocodingViewModel @Inject constructor(
    private val localGeocodingRepository: LocalGeocodingRepository,
    private val geocodingRepository: GeocodingRepository,
    private val dispatcherProvider: DispatcherProvider
) : StateReducerViewModel<GeoState, GeoEvent, GeoAction>() {

    override fun initState(): GeoState = GeoState.initial

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

                    } else {
                        state.handleEvent(GeoEvent.Error("No results"))
                    }
                }

                is Resource.Error -> {
                    state.handleEvent(GeoEvent.Error("No results"))
                    actionChannel.send(GeoAction.ShowToast("Issue loading cache!!!"))
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
        viewModelScope.launch(dispatcherProvider.io) {
            when (val result = geocodingRepository.getGeoLocation(query)) {
                is Resource.Success -> {
                    if (result.data.isNotEmpty()) {
                        state.handleEvent(GeoEvent.Loaded(result.data.map { mapped ->
                            val cachedGeoLocations = loadCache()
                            val found = cachedGeoLocations.firstOrNull {
                                it.longitude == mapped.longitude && it.latitude == mapped.latitude
                            }
                            mapped.copy(cached = found != null)
                        }))
                    } else {
                        state.handleEvent(GeoEvent.Error("No results"))
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
            geoLocations = emptyList()
        )
    }

    private fun onSaveGeoLocation(currentState: GeoState, geoLocation: GeoLocation): GeoState {
        viewModelScope.launch(dispatcherProvider.io) {
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
                    actionChannel.send(GeoAction.ShowToast("Issue saving!!!"))
                }
            }
        }
        return currentState
    }

    private fun onDeleteGeoLocation(currentState: GeoState, geoLocation: GeoLocation): GeoState {
        viewModelScope.launch(dispatcherProvider.io) {
            when (localGeocodingRepository.deleteCacheGeoLocation(geoLocation)) {
                is Resource.Success -> {
                    val geoLocations = state.value.geoLocations
                    state.handleEvent(GeoEvent.Loaded(geoLocations.minus(geoLocation)))
                }

                is Resource.Error -> {
                    actionChannel.send(GeoAction.ShowToast("Issue deleting!!!"))
                }
            }
        }
        return currentState
    }

    private fun onHandleError(currentState: GeoState, message: String): GeoState {
        return currentState.copy(
            isLoading = false,
            error = message,
            geoLocations = emptyList()
        )
    }
}