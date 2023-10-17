package com.trifork.feature.weather.presentation.mvi

sealed interface WeatherEvent {
    data class LoadWeatherInfo(val geoLocation: com.trifork.feature.weather.domain.model.WeatherLocation) :
        WeatherEvent
    data class UpdateHourlyInfo(val weatherInfo: com.trifork.feature.weather.domain.model.WeatherInfo) :
        WeatherEvent
    data class Error(val message: String) : WeatherEvent
}