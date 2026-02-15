package com.codeskraps.maps.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RainViewerDto(
    val version: String,
    val generated: Long,
    val host: String,
    val radar: RadarDataDto
)

@JsonClass(generateAdapter = true)
data class RadarDataDto(
    val past: List<RadarFrameDto>,
    val nowcast: List<RadarFrameDto>
)

@JsonClass(generateAdapter = true)
data class RadarFrameDto(
    val time: Long,
    val path: String
)
