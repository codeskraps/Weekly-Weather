package com.codeskraps.maps.presentation.mvi

sealed interface RadarAction {
    data class ShowToast(val message: String) : RadarAction
}
