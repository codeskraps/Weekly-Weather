package com.trifork.feature.geocoding.presentation.mvi

import com.trifork.feature.common.domain.model.GeoLocation


data class GeoState(
    val geoLocations: List<GeoLocation>,
    val error: String?,
    val isLoading: Boolean
) {
    companion object {
        val initial = GeoState(
            geoLocations = emptyList(),
            error = null,
            isLoading = true
        )
    }
}