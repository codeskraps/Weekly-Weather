package com.codeskraps.feature.radar.domain.model

data class RadarFrame(
    val timestamp: Long,
    val tileUrl: String
)

data class RadarData(
    val past: List<RadarFrame>,
    val nowcast: List<RadarFrame>
) {
    val allFrames: List<RadarFrame> get() = past + nowcast
}
