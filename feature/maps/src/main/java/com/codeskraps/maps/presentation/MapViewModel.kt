package com.codeskraps.maps.presentation

import androidx.lifecycle.viewModelScope
import com.codeskraps.core.local.domain.model.GeoLocation
import com.codeskraps.core.local.domain.repository.LocalGeocodingRepository
import com.codeskraps.core.local.domain.repository.LocalResourceRepository
import com.codeskraps.feature.common.dispatcher.DispatcherProvider
import com.codeskraps.feature.common.mvi.StateReducerViewModel
import com.codeskraps.feature.common.util.Resource
import com.codeskraps.location.domain.LocationTracker
import com.codeskraps.maps.presentation.mvi.MapAction
import com.codeskraps.maps.presentation.mvi.MapEvent
import com.codeskraps.maps.presentation.mvi.MapState
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
) : StateReducerViewModel<MapState, MapEvent, MapAction>() {
    override fun initState(): MapState = MapState.initial

    override fun reduceState(currentState: MapState, event: MapEvent): MapState {
        return when (event) {
            is MapEvent.Resume -> onResume(currentState)
            is MapEvent.Location -> onLocation(currentState, event.location)
            is MapEvent.LoadCache -> onLoadCache(currentState)
            is MapEvent.Loaded -> onGeoLocationsLoaded(currentState, event.geoLocations)
            is MapEvent.Error -> currentState.copy(error = event.message)
        }
    }

    private fun onResume(currentState: MapState): MapState {
        viewModelScope.launch(dispatcherProvider.io) {
            locationTracker.getCurrentLocation()?.let {
                state.handleEvent(MapEvent.Location(LatLng(it.latitude, it.longitude)))
            }
        }
        state.handleEvent(MapEvent.LoadCache)
        return currentState
    }

    private fun onLocation(currentState: MapState, location: LatLng): MapState {
        return currentState.copy(
            location = location
        )
    }

    private fun onLoadCache(currentState: MapState): MapState {
        viewModelScope.launch(dispatcherProvider.io) {
            when (val result = localGeocodingRepository.getCachedGeoLocation()) {
                is Resource.Success -> {
                    if (result.data.isNotEmpty()) {
                        val cachedGeoLocations = result.data.sortedBy { it.name }
                        state.handleEvent(MapEvent.Loaded(cachedGeoLocations))

                    } else {
                        state.handleEvent(MapEvent.Error(localResources.getNoResultString()))
                    }
                }

                is Resource.Error -> {
                    state.handleEvent(MapEvent.Error(localResources.getNoResultString()))
                    actionChannel.send(MapAction.ShowToast(localResources.getIssueLoadingCache()))
                }
            }
        }
        return currentState
    }

    private fun onGeoLocationsLoaded(
        currentState: MapState,
        geoLocations: List<GeoLocation>
    ): MapState {
        return currentState.copy(
            geoLocations = geoLocations,
            error = null
        )
    }
}