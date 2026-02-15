package com.codeskraps.maps.presentation.mvi

import com.codeskraps.maps.domain.model.RadarFrame
import com.google.android.gms.maps.model.LatLng

data class RadarState(
    val isLoading: Boolean,
    val radarFrames: List<RadarFrame>,
    val currentFrameIndex: Int,
    val isPlaying: Boolean,
    val isDoubleSpeed: Boolean,
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
            isDoubleSpeed = false,
            userLocation = null,
            error = null
        )
    }
}
