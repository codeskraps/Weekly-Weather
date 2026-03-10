package com.codeskraps.maps.data.remote.wind

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WindLocationDto(
    val latitude: Double,
    val longitude: Double,
    val current: WindCurrentDto
)

@JsonClass(generateAdapter = true)
data class WindCurrentDto(
    @param:Json(name = "wind_speed_10m") val windSpeed10m: Float,
    @param:Json(name = "wind_direction_10m") val windDirection10m: Float
)
