package com.example.weather.feature.geocoding.presentation.mvi

sealed interface GeoAction {
    data class ShowToast(val message: String) : GeoAction
}