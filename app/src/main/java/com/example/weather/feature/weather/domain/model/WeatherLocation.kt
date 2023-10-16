package com.example.weather.feature.weather.domain.model

data class WeatherLocation(
    val name: String = "Current Location",
    val lat: Double = 0.0,
    val long: Double = 0.0
)
