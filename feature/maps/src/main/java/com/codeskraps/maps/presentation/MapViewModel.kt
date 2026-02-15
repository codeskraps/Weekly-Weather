package com.codeskraps.maps.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.codeskraps.core.local.domain.model.GeoLocation
import com.codeskraps.core.local.domain.repository.LocalGeocodingRepository
import com.codeskraps.core.local.domain.repository.LocalResourceRepository
import com.codeskraps.core.local.domain.repository.SettingsRepository
import com.codeskraps.core.location.domain.LocationTracker
import com.codeskraps.feature.common.dispatcher.DispatcherProvider
import com.codeskraps.feature.common.domain.model.ActiveLocation
import com.codeskraps.feature.common.domain.repository.ActiveLocationRepository
import com.codeskraps.feature.common.mvi.StateReducerViewModel
import com.codeskraps.feature.common.util.Resource
import com.codeskraps.maps.domain.repository.GeocodingRepository
import com.codeskraps.maps.presentation.mvi.MapAction
import com.codeskraps.maps.presentation.mvi.MapEvent
import com.codeskraps.maps.presentation.mvi.MapState
import com.codeskraps.umami.domain.AnalyticsRepository
import com.google.android.gms.maps.model.LatLng
import kotlin.math.abs
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MapViewModel(
    private val locationTracker: LocationTracker,
    private val localGeocodingRepository: LocalGeocodingRepository,
    private val geocodingRepository: GeocodingRepository,
    private val localResources: LocalResourceRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val analytics: AnalyticsRepository,
    private val activeLocationRepository: ActiveLocationRepository,
    private val settingsRepository: SettingsRepository
) : StateReducerViewModel<MapState, MapEvent, MapAction>(MapState.initial) {

    private companion object {
        private const val TAG = "WeatherApp:MapViewModel"
        private const val SCREEN_NAME = "map_screen"
        private const val EVENT_LOCATION_UPDATED = "map_location_updated"
        private const val EVENT_ERROR = "map_error"
        private const val EVENT_SEARCH = "map_search"
        private const val EVENT_SAVE = "map_save_location"
        private const val EVENT_DELETE = "map_delete_location"
        private const val EVENT_SELECT = "map_select_location"

        private const val PARAM_LATITUDE = "latitude"
        private const val PARAM_LONGITUDE = "longitude"
        private const val PARAM_ERROR = "error_message"
        private const val PARAM_QUERY = "query"
        private const val PARAM_RESULTS = "results_count"
        private const val PARAM_LOCATION = "location"
    }

    private var mapLocationString: String = "Map Location"
    private var currentLocationString: String = "Current Location"
    private var searchJob: Job? = null
    private var gpsTrackingJob: Job? = null
    private var persistedZoom: Float = SettingsRepository.DEFAULT_MAP_ZOOM

    init {
        Log.i(TAG, "Initializing MapViewModel")
        viewModelScope.launch(dispatcherProvider.io) {
            mapLocationString = localResources.getMapLocationString()
            currentLocationString = localResources.getCurrentLocationString()
            persistedZoom = settingsRepository.getMapZoom()
            analytics.trackPageView(SCREEN_NAME)
        }
    }

    override fun reduceState(currentState: MapState, event: MapEvent): MapState {
        Log.i(TAG, "Reducing state for event: $event")

        return when (event) {
            MapEvent.Resume -> {
                Log.i(TAG, "Handling Resume event")
                val sharedLocation = activeLocationRepository.location.value
                if (sharedLocation != null) {
                    val isGps = sharedLocation.isGpsLocation
                    Log.i(TAG, "Using shared location: ${sharedLocation.name}, isGps=$isGps, zoom: $persistedZoom")
                    if (isGps) startGpsTracking() else stopGpsTracking()
                    currentState.copy(
                        location = LatLng(sharedLocation.latitude, sharedLocation.longitude),
                        locationName = sharedLocation.name,
                        zoom = persistedZoom,
                        isGpsTracking = isGps
                    )
                } else {
                    startGpsTracking()
                    currentState.copy(
                        zoom = persistedZoom,
                        isGpsTracking = true,
                        locationName = currentLocationString
                    )
                }
            }
            is MapEvent.Location -> {
                Log.i(TAG, "Handling Location event: ${event.location}")
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
            is MapEvent.CameraIdle -> {
                Log.i(TAG, "Camera idle at: ${event.location}, zoom: ${event.zoom}")
                stopGpsTracking()
                val stateLocation = currentState.location
                val cameraMoved = stateLocation == null ||
                    abs(stateLocation.latitude - event.location.latitude) > 1e-4 ||
                    abs(stateLocation.longitude - event.location.longitude) > 1e-4
                val name = if (cameraMoved) mapLocationString
                    else currentState.locationName.ifEmpty { mapLocationString }
                // When camera hasn't meaningfully moved, preserve the original exact
                // coordinates so isCached exact-match queries still work
                val lat = if (!cameraMoved && stateLocation != null) stateLocation.latitude else event.location.latitude
                val lng = if (!cameraMoved && stateLocation != null) stateLocation.longitude else event.location.longitude
                activeLocationRepository.update(
                    ActiveLocation(
                        name = name,
                        latitude = lat,
                        longitude = lng,
                        isGpsLocation = !cameraMoved && currentState.isGpsTracking
                    )
                )
                persistedZoom = event.zoom
                viewModelScope.launch(dispatcherProvider.io) {
                    settingsRepository.setMapZoom(event.zoom)
                }
                currentState.copy(
                    zoom = event.zoom,
                    locationName = if (cameraMoved) "" else currentState.locationName,
                    isGpsTracking = false
                )
            }
            MapEvent.CameraMoved -> {
                stopGpsTracking()
                currentState.copy(locationName = "", isGpsTracking = false)
            }
            MapEvent.ToggleGps -> {
                if (currentState.isGpsTracking) {
                    stopGpsTracking()
                    currentState.copy(isGpsTracking = false)
                } else {
                    startGpsTracking()
                    // Clear location so the next GPS update triggers LaunchedEffect
                    currentState.copy(
                        location = null,
                        isGpsTracking = true,
                        locationName = currentLocationString
                    )
                }
            }
            is MapEvent.Error -> {
                Log.e(TAG, "Handling Error event: ${event.message}")
                viewModelScope.launch(dispatcherProvider.io) {
                    analytics.trackEvent(EVENT_ERROR, mapOf(PARAM_ERROR to event.message))
                    actionChannel.send(MapAction.ShowToast(event.message))
                }
                currentState.copy(
                    error = event.message,
                    isLoading = false
                )
            }

            is MapEvent.Search -> onSearch(currentState, event.query)
            is MapEvent.SearchResultsLoaded -> currentState.copy(
                searchResults = event.geoLocations,
                isSearchLoading = false,
                searchError = null
            )
            is MapEvent.SearchError -> currentState.copy(
                isSearchLoading = false,
                searchError = event.message,
                searchResults = emptyList()
            )
            is MapEvent.SearchFocusChanged -> onSearchFocusChanged(currentState, event.focused)
            MapEvent.DismissSearch -> onDismissSearch(currentState)
            is MapEvent.SaveLocation -> onSaveLocation(currentState, event.geoLocation)
            is MapEvent.DeleteLocation -> onDeleteLocation(currentState, event.geoLocation)
            is MapEvent.SelectLocation -> onSelectLocation(currentState, event.geoLocation)
            MapEvent.ToggleRadarMode -> currentState.copy(isRadarMode = !currentState.isRadarMode)
        }
    }

    private fun onSearch(currentState: MapState, query: String): MapState {
        searchJob?.cancel()
        if (query.length >= 3) {
            searchJob = viewModelScope.launch(dispatcherProvider.io) {
                delay(500L)
                when (val result = geocodingRepository.getGeoLocation(query)) {
                    is Resource.Success -> {
                        if (result.data.isNotEmpty()) {
                            val cachedLocations = loadCache()
                            val mappedLocations = result.data.map { mapped ->
                                val found = cachedLocations.firstOrNull {
                                    it.longitude == mapped.longitude && it.latitude == mapped.latitude
                                }
                                mapped.copy(cached = found != null)
                            }
                            analytics.trackEvent(
                                EVENT_SEARCH,
                                mapOf(
                                    PARAM_QUERY to query,
                                    PARAM_RESULTS to mappedLocations.size.toString()
                                )
                            )
                            state.handleEvent(MapEvent.SearchResultsLoaded(mappedLocations))
                        } else {
                            state.handleEvent(MapEvent.SearchError(localResources.getNoResultString()))
                        }
                    }
                    is Resource.Error -> {
                        state.handleEvent(MapEvent.SearchError(result.message))
                    }
                }
            }
        } else if (query.isEmpty()) {
            viewModelScope.launch(dispatcherProvider.io) {
                val cached = loadCache().sortedBy { it.name }
                state.handleEvent(MapEvent.SearchResultsLoaded(cached))
            }
        }

        return currentState.copy(
            searchQuery = query,
            isSearchLoading = query.length >= 3,
            searchError = null
        )
    }

    private fun onSearchFocusChanged(currentState: MapState, focused: Boolean): MapState {
        if (focused && currentState.searchQuery.isEmpty()) {
            viewModelScope.launch(dispatcherProvider.io) {
                val cached = loadCache().sortedBy { it.name }
                state.handleEvent(MapEvent.SearchResultsLoaded(cached))
            }
        }
        return currentState.copy(isSearchFocused = focused)
    }

    private fun onDismissSearch(currentState: MapState): MapState {
        searchJob?.cancel()
        viewModelScope.launch {
            actionChannel.send(MapAction.ClearSearchFocus)
        }
        return currentState.copy(
            searchQuery = "",
            searchResults = emptyList(),
            isSearchFocused = false,
            isSearchLoading = false,
            searchError = null
        )
    }

    private fun onSelectLocation(currentState: MapState, geoLocation: GeoLocation): MapState {
        searchJob?.cancel()
        stopGpsTracking()
        val latLng = LatLng(geoLocation.latitude, geoLocation.longitude)
        activeLocationRepository.update(
            ActiveLocation(
                name = geoLocation.displayName(),
                latitude = geoLocation.latitude,
                longitude = geoLocation.longitude,
                isGpsLocation = false
            )
        )
        viewModelScope.launch(dispatcherProvider.io) {
            analytics.trackEvent(
                EVENT_SELECT,
                mapOf(PARAM_LOCATION to geoLocation.displayName())
            )
            actionChannel.send(MapAction.ClearSearchFocus)
        }
        return currentState.copy(
            location = latLng,
            locationName = geoLocation.displayName(),
            isGpsTracking = false,
            searchQuery = "",
            searchResults = emptyList(),
            isSearchFocused = false,
            isSearchLoading = false,
            searchError = null
        )
    }

    private fun onSaveLocation(currentState: MapState, geoLocation: GeoLocation): MapState {
        val currentResults = currentState.searchResults
        viewModelScope.launch(dispatcherProvider.io) {
            analytics.trackEvent(
                EVENT_SAVE,
                mapOf(PARAM_LOCATION to geoLocation.name)
            )
            when (localGeocodingRepository.saveCacheGeoLocation(geoLocation)) {
                is Resource.Success -> {
                    val updatedResults = currentResults.map {
                        if (geoLocation.latitude == it.latitude && geoLocation.longitude == it.longitude) {
                            it.copy(cached = true)
                        } else it
                    }
                    state.handleEvent(MapEvent.SearchResultsLoaded(updatedResults))
                }
                is Resource.Error -> {
                    actionChannel.send(MapAction.ShowToast(localResources.getIssueSaving()))
                }
            }
        }
        return currentState
    }

    private fun onDeleteLocation(currentState: MapState, geoLocation: GeoLocation): MapState {
        val currentResults = currentState.searchResults
        viewModelScope.launch(dispatcherProvider.io) {
            analytics.trackEvent(
                EVENT_DELETE,
                mapOf(PARAM_LOCATION to geoLocation.name)
            )
            when (localGeocodingRepository.deleteCacheGeoLocation(geoLocation)) {
                is Resource.Success -> {
                    val updatedResults = currentResults.map {
                        if (geoLocation.latitude == it.latitude && geoLocation.longitude == it.longitude) {
                            it.copy(cached = false)
                        } else it
                    }
                    state.handleEvent(MapEvent.SearchResultsLoaded(updatedResults))
                }
                is Resource.Error -> {
                    actionChannel.send(MapAction.ShowToast(localResources.getIssueDeleting()))
                }
            }
        }
        return currentState
    }

    private fun startGpsTracking() {
        gpsTrackingJob?.cancel()
        gpsTrackingJob = viewModelScope.launch(dispatcherProvider.io) {
            while (true) {
                locationTracker.getCurrentLocation()?.let { location ->
                    val latLng = LatLng(location.latitude, location.longitude)
                    state.handleEvent(MapEvent.Location(latLng))
                    activeLocationRepository.update(
                        ActiveLocation(
                            name = currentLocationString,
                            latitude = location.latitude,
                            longitude = location.longitude,
                            isGpsLocation = true
                        )
                    )
                }
                delay(3000)
            }
        }
    }

    private fun stopGpsTracking() {
        gpsTrackingJob?.cancel()
        gpsTrackingJob = null
    }

    override fun onCleared() {
        super.onCleared()
        gpsTrackingJob?.cancel()
    }

    private suspend fun loadCache(): List<GeoLocation> {
        return when (val result = localGeocodingRepository.getCachedGeoLocation()) {
            is Resource.Success -> result.data.ifEmpty { emptyList() }
            is Resource.Error -> emptyList()
        }
    }
}
