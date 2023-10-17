package com.trifork.feature.geocoding.data.remote

data class GeoLocationDto(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val admin1: String?
)
