package com.trifork.feature.common.data.mappers

import com.trifork.feature.common.data.local.GeoLocationEntity
import com.trifork.feature.common.domain.model.GeoLocation

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
        uid = kotlin.random.Random.nextInt(),
        name = name,
        latitude = latitude,
        longitude = longitude,
        country = country,
        admin1 = admin1
    )
}