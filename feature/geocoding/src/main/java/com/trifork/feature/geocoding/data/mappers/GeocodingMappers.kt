package com.trifork.feature.geocoding.data.mappers

import com.trifork.feature.geocoding.data.local.GeoLocationEntity
import com.trifork.feature.geocoding.data.remote.GeocodingDto
import com.trifork.feature.geocoding.domain.model.GeoLocation
import kotlin.random.Random

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

fun GeoLocationEntity.toGeocoding(): GeoLocation {
    return GeoLocation(
        name = name,
        latitude = latitude,
        longitude = longitude,
        country = country,
        admin1 = admin1,
        cached = true
    )
}

fun GeoLocation.toGeoLocationEntity(): GeoLocationEntity {
    return GeoLocationEntity(
        uid = Random.nextInt(),
        name = name,
        latitude = latitude,
        longitude = longitude,
        country = country,
        admin1 = admin1
    )
}