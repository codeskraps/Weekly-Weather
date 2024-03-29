package com.codeskraps.core.local.domain.model

data class GeoLocation(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val admin1: String?,
    val cached: Boolean = false
) {
    fun displayName(): String {
        return if (country.isBlank()) {
            name
        } else {
            "$name, $country"
        }
    }
}