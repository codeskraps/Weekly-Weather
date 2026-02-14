package com.codeskraps.feature.weather.domain.repository


import com.codeskraps.feature.common.util.Resource
import com.codeskraps.feature.weather.domain.model.WeatherInfo

interface WeatherRepository {

    suspend fun getWeatherData(
        lat: Double,
        long: Double,
        temperatureUnit: String = "celsius",
        windSpeedUnit: String = "kmh"
    ): Resource<WeatherInfo>
}