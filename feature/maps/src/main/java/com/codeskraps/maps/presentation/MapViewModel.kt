package com.codeskraps.maps.presentation

import androidx.lifecycle.viewModelScope
import com.codeskraps.core.local.domain.repository.LocalGeocodingRepository
import com.codeskraps.core.local.domain.repository.LocalResourceRepository
import com.codeskraps.core.location.domain.LocationTracker
import com.codeskraps.feature.common.dispatcher.DispatcherProvider
import com.codeskraps.feature.common.mvi.StateReducerViewModel
import com.codeskraps.feature.common.util.Resource
import com.codeskraps.maps.presentation.mvi.MapAction
import com.codeskraps.maps.presentation.mvi.MapEvent
import com.codeskraps.maps.presentation.mvi.MapState
import com.codeskraps.umami.domain.AnalyticsRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationTracker: LocationTracker,
    private val localGeocodingRepository: LocalGeocodingRepository,
    private val localResources: LocalResourceRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val analytics: AnalyticsRepository
) : StateReducerViewModel<MapState, MapEvent, MapAction>(MapState.initial) {

    private companion object {
        private const val TAG = "WeatherApp:MapViewModel"
        private const val SCREEN_NAME = "map_screen"
        private const val EVENT_LOCATION_UPDATED = "map_location_updated"
        private const val EVENT_LOCATIONS_LOADED = "map_locations_loaded"
        private const val EVENT_ERROR = "map_error"

        private const val PARAM_LATITUDE = "latitude"
        private const val PARAM_LONGITUDE = "longitude"
        private const val PARAM_ERROR = "error_message"
        private const val PARAM_LOCATIONS_COUNT = "locations_count"
    }

    init {
        android.util.Log.i(TAG, "Initializing MapViewModel")
        viewModelScope.launch(dispatcherProvider.io) {
            analytics.trackPageView(SCREEN_NAME)
        }
    }

    override fun reduceState(currentState: MapState, event: MapEvent): MapState {
        android.util.Log.i(TAG, "Reducing state for event: $event, current state: $currentState")
        
        return when (event) {
            MapEvent.Resume -> {
                android.util.Log.i(TAG, "Handling Resume event")
                viewModelScope.launch(dispatcherProvider.io) {
                    locationTracker.getCurrentLocation()?.let {
                        android.util.Log.i(TAG, "Current location obtained: ${it.latitude}, ${it.longitude}")
                        state.handleEvent(MapEvent.Location(LatLng(it.latitude, it.longitude)))
                    } ?: android.util.Log.w(TAG, "Failed to get current location")
                    loadCachedLocations()
                }
                currentState.copy(isLoading = true)
            }
            is MapEvent.Location -> {
                android.util.Log.i(TAG, "Handling Location event: ${event.location}")
                viewModelScope.launch(dispatcherProvider.io) {
                    analytics.trackEvent(
                        EVENT_LOCATION_UPDATED,
                        mapOf(
                            PARAM_LATITUDE to event.location.latitude.toString(),
                            PARAM_LONGITUDE to event.location.longitude.toString()
                        )
                    )
                }
                currentState.copy(location = event.location)
            }
            MapEvent.LoadCache -> {
                android.util.Log.i(TAG, "Handling LoadCache event")
                viewModelScope.launch(dispatcherProvider.io) {
                    loadCachedLocations()
                }
                currentState.copy(isLoading = true)
            }
            is MapEvent.Loaded -> {
                android.util.Log.i(TAG, "Handling Loaded event with ${event.geoLocations.size} locations")
                viewModelScope.launch(dispatcherProvider.io) {
                    analytics.trackEvent(
                        EVENT_LOCATIONS_LOADED,
                        mapOf(PARAM_LOCATIONS_COUNT to event.geoLocations.size.toString())
                    )
                }
                currentState.copy(
                    geoLocations = event.geoLocations,
                    error = null,
                    isLoading = false
                )
            }
            is MapEvent.Error -> {
                android.util.Log.e(TAG, "Handling Error event: ${event.message}")
                viewModelScope.launch(dispatcherProvider.io) {
                    analytics.trackEvent(EVENT_ERROR, mapOf(PARAM_ERROR to event.message))
                    actionChannel.send(MapAction.ShowToast(event.message))
                }
                currentState.copy(
                    error = event.message,
                    isLoading = false
                )
            }
        }
    }

    private suspend fun loadCachedLocations() {
        android.util.Log.i(TAG, "Loading cached locations")
        when (val result = localGeocodingRepository.getCachedGeoLocation()) {
            is Resource.Success -> {
                if (result.data.isNotEmpty()) {
                    android.util.Log.i(TAG, "Successfully loaded ${result.data.size} locations")
                    val cachedGeoLocations = result.data.sortedBy { it.name }
                    state.handleEvent(MapEvent.Loaded(cachedGeoLocations))
                } else {
                    android.util.Log.w(TAG, "No cached locations found")
                    state.handleEvent(MapEvent.Error(localResources.getNoResultString()))
                }
            }
            is Resource.Error -> {
                android.util.Log.e(TAG, "Error loading cached locations: ${result.message}")
                state.handleEvent(MapEvent.Error(localResources.getNoResultString()))
                actionChannel.send(MapAction.ShowToast(localResources.getIssueLoadingCache()))
            }
        }
    }
}