package com.codeskraps.feature.radar.presentation.mvi

import com.codeskraps.feature.radar.domain.model.RadarFrame
import com.google.android.gms.maps.model.LatLng

sealed interface RadarEvent {
    data object Resume : RadarEvent
    data class RadarDataLoaded(val frames: List<RadarFrame>) : RadarEvent
    data class Error(val message: String) : RadarEvent
    data object PlayPause : RadarEvent
    data class SeekToFrame(val index: Int) : RadarEvent
    data object NextFrame : RadarEvent
    data class LocationUpdated(val location: LatLng) : RadarEvent
}
