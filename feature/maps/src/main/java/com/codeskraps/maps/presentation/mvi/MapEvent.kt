package com.codeskraps.maps.presentation.mvi

import com.codeskraps.core.local.domain.model.GeoLocation
import com.google.android.gms.maps.model.LatLng

sealed interface MapEvent {
    data object Resume : MapEvent
    data class Location(val location: LatLng) : MapEvent
    data class CameraIdle(val location: LatLng, val zoom: Float) : MapEvent
    data object CameraMoved : MapEvent
    data object ToggleGps : MapEvent
    data class Error(val message: String) : MapEvent

    data class Search(val query: String) : MapEvent
    data class SearchResultsLoaded(val geoLocations: List<GeoLocation>) : MapEvent
    data class SearchError(val message: String) : MapEvent
    data class SearchFocusChanged(val focused: Boolean) : MapEvent
    data object DismissSearch : MapEvent
    data class SaveLocation(val geoLocation: GeoLocation) : MapEvent
    data class DeleteLocation(val geoLocation: GeoLocation) : MapEvent
    data class SelectLocation(val geoLocation: GeoLocation) : MapEvent
    data object ToggleRadarMode : MapEvent
}