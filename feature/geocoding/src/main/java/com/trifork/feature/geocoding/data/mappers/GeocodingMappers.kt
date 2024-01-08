package com.trifork.feature.geocoding.data.mappers

import com.trifork.feature.common.domain.model.GeoLocation
import com.trifork.feature.geocoding.data.remote.GeocodingDto

fun GeocodingDto.toGeocoding(): List<GeoLocation> {
    return results.map {
        GeoLocation(
            name = it.name,
            latitude = it.latitude,
            longitude = it.longitude,
            country = it.country,
            admin1 = it.admin1 ?: "",
            cached = false
        )
    }
}
