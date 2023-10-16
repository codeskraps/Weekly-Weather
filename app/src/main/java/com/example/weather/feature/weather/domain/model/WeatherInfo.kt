package com.example.weather.feature.weather.domain.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

data class WeatherInfo(
    val geoLocation: String = "",
    val weatherDataPerDay: ImmutableMap<Int, ImmutableList<WeatherData>>,
    val currentWeatherData: WeatherData?,
)
