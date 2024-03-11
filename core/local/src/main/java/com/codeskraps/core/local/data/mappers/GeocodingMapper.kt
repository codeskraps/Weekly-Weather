package com.codeskraps.core.local.data.mappers

import com.codeskraps.core.local.data.model.GeoLocationEntity
import com.codeskraps.core.local.domain.model.GeoLocation
import kotlin.random.Random

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