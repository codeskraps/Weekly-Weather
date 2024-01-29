package com.codeskraps.feature.weather.domain.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

data class WeatherInfo(
    val geoLocation: String = "",
    val latitude: Double = .0,
    val longitude: Double = .0,
    val weatherDataPerDay: ImmutableMap<Int, ImmutableList<WeatherData>>,
    val currentWeatherData: WeatherData?,
)
