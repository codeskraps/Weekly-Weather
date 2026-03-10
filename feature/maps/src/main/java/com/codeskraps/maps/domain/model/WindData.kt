package com.codeskraps.maps.domain.model

data class WindPoint(
    val latitude: Double,
    val longitude: Double,
    val speedMs: Float,
    val directionDeg: Float
)

data class WindData(
    val points: List<WindPoint>
)
