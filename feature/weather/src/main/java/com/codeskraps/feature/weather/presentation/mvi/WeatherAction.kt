package com.codeskraps.feature.weather.presentation.mvi

sealed interface WeatherAction {
    data class Toast(val message: String) : WeatherAction
}