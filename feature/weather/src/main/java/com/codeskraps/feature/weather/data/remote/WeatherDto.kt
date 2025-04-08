package com.codeskraps.feature.weather.data.remote

import com.squareup.moshi.Json

data class WeatherDto(
    @field:Json(name = "hourly")
    val hourly: HourlyDto,
    @field:Json(name = "daily")
    val daily: SunDataDto
)

data class HourlyDto(
    val time: List<String>,
    @field:Json(name = "temperature_2m")
    val temperature_2m: List<Double>,
    val weathercode: List<Int>,
    @field:Json(name = "pressure_msl")
    val pressure_msl: List<Double>,
    @field:Json(name = "windspeed_10m")
    val windspeed_10m: List<Double>,
    @field:Json(name = "relativehumidity_2m")
    val relativehumidity_2m: List<Double>
)
