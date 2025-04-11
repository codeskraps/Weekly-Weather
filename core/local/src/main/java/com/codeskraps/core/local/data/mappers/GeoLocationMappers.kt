package com.codeskraps.core.local.data.mappers

import com.codeskraps.core.local.data.model.GeoLocationEntity
import com.codeskraps.core.local.domain.model.GeoLocation

fun GeoLocation.toGeoLocationEntity(): GeoLocationEntity {
    return GeoLocationEntity(
        uid = 0, // Room will auto-generate this
        name = name,
        latitude = latitude,
        longitude = longitude,
        country = country,
        admin1 = admin1
    )
}

fun GeoLocationEntity.toGeoLocation(): GeoLocation {
    return GeoLocation(
        name = name,
        latitude = latitude,
        longitude = longitude,
        country = country,
        admin1 = admin1
    )
} 