package com.codeskraps.maps.presentation.mvi

import com.codeskraps.maps.domain.model.WindData

data class WindState(
    val isLoading: Boolean,
    val windData: WindData?,
    val error: String?,
) {
    companion object {
        val initial = WindState(
            isLoading = false,
            windData = null,
            error = null
        )
    }
}
