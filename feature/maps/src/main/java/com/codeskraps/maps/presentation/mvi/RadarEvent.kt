package com.codeskraps.maps.presentation.mvi

import com.codeskraps.maps.domain.model.RadarFrame
import com.google.android.gms.maps.model.LatLng

sealed interface RadarEvent {
    data object Resume : RadarEvent
    data object Pause : RadarEvent
    data class RadarDataLoaded(val frames: List<RadarFrame>, val isDoubleSpeed: Boolean) : RadarEvent
    data class Error(val message: String) : RadarEvent
    data object PlayPause : RadarEvent
    data class SeekToFrame(val index: Int) : RadarEvent
    data object NextFrame : RadarEvent
    data class LocationUpdated(val location: LatLng) : RadarEvent
    data object ToggleSpeed : RadarEvent
}
