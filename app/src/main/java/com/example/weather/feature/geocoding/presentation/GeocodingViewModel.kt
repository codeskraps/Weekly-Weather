package com.example.weather.feature.geocoding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.core.domain.util.Resource
import com.example.weather.feature.common.dispatcher.DispatcherProvider
import com.example.weather.feature.common.mvi.StateReducerFlow
import com.example.weather.feature.geocoding.domain.model.GeoLocation
import com.example.weather.feature.geocoding.domain.repository.GeocodingRepository
import com.example.weather.feature.geocoding.presentation.mvi.GeoAction
import com.example.weather.feature.geocoding.presentation.mvi.GeoEvent
import com.example.weather.feature.geocoding.presentation.mvi.GeoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeocodingViewModel @Inject constructor(
    private val geocodingRepository: GeocodingRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    val state = StateReducerFlow(
        initialState = GeoState.initial,
        reduceState = ::reduceState,
    )
    private val _action = MutableSharedFlow<GeoAction>()
    val action = _action.asSharedFlow()

    private fun reduceState(
        currentState: GeoState,
        event: GeoEvent
    ): GeoState {
        return when (event) {
            is GeoEvent.Search -> searchGeoLocation(currentState, event.query)
            is GeoEvent.Delete -> deleteGeoLocation(currentState, event.geoLocation)
            is GeoEvent.Save -> saveGeoLocation(currentState, event.geoLocation)
            is GeoEvent.Loaded -> geoLocationsLoaded(currentState, event.geoLocations)
            is GeoEvent.Error -> handleError(currentState, event.message)
            is GeoEvent.LoadCache -> loadCache(currentState)
        }
    }

    private fun loadCache(currentState: GeoState): GeoState {
        viewModelScope.launch(dispatcherProvider.io) {
            when (val result = geocodingRepository.getCachedGeoLocation()) {
                is Resource.Success -> {
                    if ((result.data?.size ?: 0) > 0) {
                        val cachedGeoLocations = result.data!!.sortedBy { it.name }
                        state.handleEvent(GeoEvent.Loaded(cachedGeoLocations))

                    } else {
                        state.handleEvent(GeoEvent.Error("No results"))
                    }
                }

                is Resource.Error -> {
                    state.handleEvent(GeoEvent.Error("No results"))
                    _action.emit(GeoAction.ShowToast("Issue loading cache!!!"))
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
        return when (val result = geocodingRepository.getCachedGeoLocation()) {
            is Resource.Success -> {
                if ((result.data?.size ?: 0) > 0) {
                    result.data!!
                } else {
                    emptyList()
                }
            }

            is Resource.Error -> emptyList()
        }
    }

    private fun geoLocationsLoaded(
        currentState: GeoState,
        geoLocations: List<GeoLocation>
    ): GeoState {
        return currentState.copy(
            isLoading = false,
            geoLocations = geoLocations,
            error = null
        )
    }

    private fun searchGeoLocation(currentState: GeoState, query: String): GeoState {
        viewModelScope.launch(dispatcherProvider.io) {
            when (val result = geocodingRepository.getGeoLocation(query)) {
                is Resource.Success -> {
                    if ((result.data?.size ?: 0) > 0) {
                        state.handleEvent(GeoEvent.Loaded(result.data!!.map { mapped ->
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
                    state.handleEvent(GeoEvent.Error(result.message!!))
                }
            }
        }
        return currentState.copy(
            isLoading = true,
            error = null,
            geoLocations = emptyList()
        )
    }

    private fun saveGeoLocation(currentState: GeoState, geoLocation: GeoLocation): GeoState {
        viewModelScope.launch(dispatcherProvider.io) {
            val geoLocations = state.value.geoLocations
            when (geocodingRepository.saveCacheGeoLocation(geoLocation)) {
                is Resource.Success -> {
                    state.handleEvent(GeoEvent.Loaded(geoLocations.map {
                        if (geoLocation.latitude == it.latitude && geoLocation.longitude == it.longitude) {
                            it.copy(cached = true)
                        } else it
                    }))
                }

                is Resource.Error -> {
                    _action.emit(GeoAction.ShowToast("Issue saving!!!"))
                }
            }
        }
        return currentState
    }

    private fun deleteGeoLocation(currentState: GeoState, geoLocation: GeoLocation): GeoState {
        viewModelScope.launch(dispatcherProvider.io) {
            when (geocodingRepository.deleteCacheGeoLocation(geoLocation)) {
                is Resource.Success -> {
                    val geoLocations = state.value.geoLocations
                    state.handleEvent(GeoEvent.Loaded(geoLocations.minus(geoLocation)))
                }

                is Resource.Error -> {
                    _action.emit(GeoAction.ShowToast("Issue deleting!!!"))
                }
            }
        }
        return currentState
    }

    private fun handleError(currentState: GeoState, message: String): GeoState {
        return currentState.copy(
            isLoading = false,
            error = message,
            geoLocations = emptyList()
        )
    }
}