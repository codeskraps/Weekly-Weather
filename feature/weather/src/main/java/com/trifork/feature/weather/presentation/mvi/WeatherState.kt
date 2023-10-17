package com.trifork.feature.weather.presentation.mvi

data class WeatherState(
    val isLoading: Boolean,
    val error: String?,
    val weatherInfo: com.trifork.feature.weather.domain.model.WeatherInfo?
) {
    companion object {
        val initial = WeatherState(
            isLoading = true,
            error = null,
            weatherInfo = null
        )
    }
}

