package com.codeskraps.maps.presentation.mvi

import com.codeskraps.maps.domain.model.WindData

sealed interface WindEvent {
    data class LoadWind(
        val latMin: Double,
        val latMax: Double,
        val lngMin: Double,
        val lngMax: Double
    ) : WindEvent

    data class WindDataLoaded(val windData: WindData) : WindEvent
    data class Error(val message: String) : WindEvent
}
