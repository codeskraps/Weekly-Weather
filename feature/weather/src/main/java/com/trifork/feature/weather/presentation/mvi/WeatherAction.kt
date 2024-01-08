package com.trifork.feature.weather.presentation.mvi

sealed interface WeatherAction {
    data class Toast(val message: String) : WeatherAction
}