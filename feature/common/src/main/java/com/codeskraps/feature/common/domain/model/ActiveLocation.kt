package com.codeskraps.feature.common.domain.model

data class ActiveLocation(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val isGpsLocation: Boolean = false
)
