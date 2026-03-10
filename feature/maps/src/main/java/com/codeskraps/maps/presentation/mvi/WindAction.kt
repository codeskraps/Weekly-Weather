package com.codeskraps.maps.presentation.mvi

sealed interface WindAction {
    data class ShowToast(val message: String) : WindAction
}
