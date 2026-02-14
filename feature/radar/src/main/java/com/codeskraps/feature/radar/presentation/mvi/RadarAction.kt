package com.codeskraps.feature.radar.presentation.mvi

sealed interface RadarAction {
    data class ShowToast(val message: String) : RadarAction
}
