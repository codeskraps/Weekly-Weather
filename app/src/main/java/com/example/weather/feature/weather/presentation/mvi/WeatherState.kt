package com.example.weather.feature.weather.presentation.mvi

import com.example.weather.feature.weather.domain.model.WeatherInfo

data class WeatherState(
    val isLoading: Boolean,
    val error: String?,
    val weatherInfo: WeatherInfo?
) {
    companion object {
        val initial = WeatherState(
            isLoading = true,
            error = null,
            weatherInfo = null
        )
    }
}

