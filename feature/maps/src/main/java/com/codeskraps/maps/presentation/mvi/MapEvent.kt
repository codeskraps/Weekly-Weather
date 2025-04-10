package com.codeskraps.maps.presentation.mvi

import com.codeskraps.core.local.domain.model.GeoLocation
import com.google.android.gms.maps.model.LatLng

sealed interface MapEvent {
    data object Resume : MapEvent
    data class Location(val location: LatLng) : MapEvent
    data object LoadCache : MapEvent
    data class Loaded(val geoLocations: List<GeoLocation>) : MapEvent
    data class Error(val message: String) : MapEvent
}