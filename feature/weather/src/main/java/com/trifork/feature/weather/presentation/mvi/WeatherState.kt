package com.trifork.feature.weather.presentation.mvi

import com.trifork.feature.weather.domain.model.WeatherInfo

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

