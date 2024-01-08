package com.trifork.feature.common.domain.model

data class GeoLocation(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val admin1: String?,
    val cached: Boolean = false
)