package com.codeskraps.maps.presentation.mvi

sealed interface MapAction {
    data class ShowToast(val message: String) : MapAction
}