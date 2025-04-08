package com.codeskraps.feature.geocoding.data.mappers

import com.codeskraps.core.local.domain.model.GeoLocation
import com.codeskraps.feature.geocoding.data.remote.GeocodingDto

fun GeocodingDto.toGeocoding(): List<GeoLocation> {
    return results.map {
        GeoLocation(
            name = it.name,
            latitude = it.latitude,
            longitude = it.longitude,
            country = it.country ?: "",
            admin1 = it.admin1,
            cached = false
        )
    }
}
