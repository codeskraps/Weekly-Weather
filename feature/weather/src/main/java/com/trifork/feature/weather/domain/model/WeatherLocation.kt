package com.trifork.feature.weather.domain.model

data class WeatherLocation(
    val name: String = "Current Location",
    val lat: Double = .0,
    val long: Double = .0
)
