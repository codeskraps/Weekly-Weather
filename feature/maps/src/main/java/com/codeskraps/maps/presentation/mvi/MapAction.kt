package com.codeskraps.maps.presentation.mvi

sealed interface MapAction {
    data class ShowToast(val message: String) : MapAction
    data object ClearSearchFocus : MapAction
    data class RestoreZoom(val zoom: Float) : MapAction
}