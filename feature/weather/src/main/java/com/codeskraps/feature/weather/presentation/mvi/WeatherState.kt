package com.codeskraps.feature.weather.presentation.mvi

import com.codeskraps.feature.weather.domain.model.WeatherInfo

data class WeatherState(
    val isLoading: Boolean,
    val error: String?,
    val weatherInfo: WeatherInfo?,
    val cached: Boolean
) {
    companion object {
        val initial = WeatherState(
            isLoading = true,
            error = null,
            weatherInfo = null,
            cached = false
        )
    }
}

