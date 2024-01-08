package com.trifork.feature.weather.presentation.mvi

import com.trifork.feature.weather.domain.model.WeatherInfo
import com.trifork.feature.weather.domain.model.WeatherLocation

sealed interface WeatherEvent {
    data class LoadWeatherInfo(val geoLocation: WeatherLocation) : WeatherEvent

    data class UpdateHourlyInfo(val weatherInfo: WeatherInfo) : WeatherEvent

    data object Refresh : WeatherEvent

    data class Error(val message: String) : WeatherEvent
    data object About : WeatherEvent
    data class CheckCache(val weatherInfo: WeatherInfo) : WeatherEvent
    data class IsCache(val isCached: Boolean) : WeatherEvent
    data object Resume : WeatherEvent
    data class Save(val weatherInfo: WeatherInfo) : WeatherEvent
    data class Delete(val weatherInfo: WeatherInfo) : WeatherEvent
}