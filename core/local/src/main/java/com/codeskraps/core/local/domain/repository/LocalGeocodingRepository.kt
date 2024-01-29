package com.codeskraps.core.local.domain.repository

import com.codeskraps.core.local.domain.model.GeoLocation
import com.codeskraps.feature.common.util.Resource

interface LocalGeocodingRepository {

    suspend fun getCachedGeoLocation(): Resource<List<GeoLocation>>

    suspend fun saveCacheGeoLocation(geoLocation: GeoLocation): Resource<Unit>

    suspend fun deleteCacheGeoLocation(geoLocation: GeoLocation): Resource<Unit>

    suspend fun isCached(latitude: Double, longitude: Double): Resource<Boolean>
}