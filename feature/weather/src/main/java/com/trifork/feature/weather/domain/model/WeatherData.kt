package com.trifork.feature.weather.domain.model

import java.time.LocalDateTime

data class WeatherData(
    val time: LocalDateTime,
    val temperatureCelsius:Double,
    val pressure:Double,
    val windSpeed:Double,
    val humidity:Double,
    val weatherType: WeatherType,
    val sunrise:LocalDateTime,
    val sunset:LocalDateTime
)
