package com.codeskraps.feature.radar.presentation.mvi

import com.codeskraps.feature.radar.domain.model.RadarFrame
import com.google.android.gms.maps.model.LatLng

data class RadarState(
    val isLoading: Boolean,
    val radarFrames: List<RadarFrame>,
    val currentFrameIndex: Int,
    val isPlaying: Boolean,
    val userLocation: LatLng?,
    val error: String?,
) {
    val currentFrame: RadarFrame? get() = radarFrames.getOrNull(currentFrameIndex)

    companion object {
        val initial = RadarState(
            isLoading = false,
            radarFrames = emptyList(),
            currentFrameIndex = 0,
            isPlaying = false,
            userLocation = null,
            error = null
        )
    }
}
