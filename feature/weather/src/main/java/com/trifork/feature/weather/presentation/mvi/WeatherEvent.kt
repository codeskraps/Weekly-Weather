package com.trifork.feature.weather.presentation.mvi

import com.trifork.feature.weather.domain.model.WeatherInfo
import com.trifork.feature.weather.domain.model.WeatherLocation

sealed interface WeatherEvent {
    data class LoadWeatherInfo(val geoLocation: WeatherLocation) :
        WeatherEvent

    data class UpdateHourlyInfo(val weatherInfo: WeatherInfo) :
        WeatherEvent

    data object Refresh : WeatherEvent

    data class Error(val message: String) : WeatherEvent
}