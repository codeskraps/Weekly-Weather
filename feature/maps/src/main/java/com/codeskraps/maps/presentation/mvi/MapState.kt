package com.codeskraps.maps.presentation.mvi

import com.codeskraps.core.local.domain.model.GeoLocation
import com.google.android.gms.maps.model.LatLng

data class MapState(
    val isLoading: Boolean,
    val location: LatLng?,
    val geoLocations: List<GeoLocation>,
    val error: String?,
) {
    companion object {
        val initial = MapState(
            isLoading = false,
            location = null,
            geoLocations = emptyList(),
            error = null
        )
    }
}
