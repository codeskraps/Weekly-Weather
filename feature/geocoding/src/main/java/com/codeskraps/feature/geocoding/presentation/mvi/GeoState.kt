package com.codeskraps.feature.geocoding.presentation.mvi

import com.codeskraps.core.local.domain.model.GeoLocation


data class GeoState(
    val geoLocations: List<GeoLocation>,
    val error: String?,
    val query: String,
    val isLoading: Boolean
) {
    companion object {
        val initial = GeoState(
            geoLocations = emptyList(),
            error = null,
            query = "",
            isLoading = true
        )
    }
}