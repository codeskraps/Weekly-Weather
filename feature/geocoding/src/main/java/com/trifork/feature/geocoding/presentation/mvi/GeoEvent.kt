package com.trifork.feature.geocoding.presentation.mvi

import com.trifork.feature.geocoding.domain.model.GeoLocation

sealed interface GeoEvent {
    data class Search(val query: String) : GeoEvent
    data class Save(val geoLocation: GeoLocation) : GeoEvent
    data class Delete(val geoLocation: GeoLocation) : GeoEvent
    data class Loaded(val geoLocations: List<GeoLocation>) : GeoEvent
    data class Error(val message: String) : GeoEvent
    data object LoadCache : GeoEvent
}