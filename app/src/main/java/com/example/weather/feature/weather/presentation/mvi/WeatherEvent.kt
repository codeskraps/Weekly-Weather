package com.example.weather.feature.weather.presentation.mvi

import com.example.weather.feature.weather.domain.model.WeatherInfo
import com.example.weather.feature.weather.domain.model.WeatherLocation

sealed interface WeatherEvent {
    data class LoadWeatherInfo(val geoLocation: WeatherLocation) : WeatherEvent
    data class UpdateHourlyInfo(val weatherInfo: WeatherInfo) : WeatherEvent
    data class Error(val message: String) : WeatherEvent
}