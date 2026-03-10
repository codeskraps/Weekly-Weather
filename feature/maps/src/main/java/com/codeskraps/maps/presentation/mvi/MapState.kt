package com.codeskraps.maps.presentation.mvi

import com.codeskraps.core.local.domain.model.GeoLocation
import com.codeskraps.core.local.domain.repository.SettingsRepository
import com.google.android.gms.maps.model.LatLng

data class MapState(
    val isLoading: Boolean,
    val location: LatLng?,
    val locationName: String,
    val zoom: Float,
    val error: String?,
    val searchQuery: String,
    val searchResults: List<GeoLocation>,
    val isSearchFocused: Boolean,
    val isSearchLoading: Boolean,
    val searchError: String?,
    val mapMode: MapMode,
    val isGpsTracking: Boolean,
) {
    val isRadarMode: Boolean get() = mapMode == MapMode.Radar
    val isWindMode: Boolean get() = mapMode == MapMode.Wind

    companion object {
        val initial = MapState(
            isLoading = false,
            location = null,
            locationName = "",
            zoom = SettingsRepository.DEFAULT_MAP_ZOOM,
            error = null,
            searchQuery = "",
            searchResults = emptyList(),
            isSearchFocused = false,
            isSearchLoading = false,
            searchError = null,
            mapMode = MapMode.Search,
            isGpsTracking = false,
        )
    }
}
