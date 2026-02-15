package com.codeskraps.feature.weather.presentation.mvi

import com.codeskraps.feature.weather.domain.model.WeatherInfo
import com.codeskraps.feature.weather.domain.model.WeatherLocation

sealed interface WeatherEvent {
    data class LoadWeatherInfo(val geoLocation: WeatherLocation) : WeatherEvent

    data class UpdateHourlyInfo(val weatherInfo: WeatherInfo, val isGpsLocation: Boolean = false) : WeatherEvent

    data object Refresh : WeatherEvent

    data class Error(val message: String) : WeatherEvent
    data object About : WeatherEvent
    data class CheckCache(val weatherLocation: WeatherLocation) : WeatherEvent
    data class IsCache(val isCached: Boolean) : WeatherEvent
    data object Resume : WeatherEvent
    data class Save(val weatherLocation: WeatherLocation) : WeatherEvent
    data class Delete(val weatherLocation: WeatherLocation) : WeatherEvent
}