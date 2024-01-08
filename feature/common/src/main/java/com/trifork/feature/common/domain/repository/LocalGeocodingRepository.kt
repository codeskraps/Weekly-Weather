package com.trifork.feature.common.domain.repository

import com.trifork.feature.common.domain.model.GeoLocation
import com.trifork.feature.common.util.Resource

interface LocalGeocodingRepository {

    suspend fun getCachedGeoLocation(): Resource<List<GeoLocation>>

    suspend fun saveCacheGeoLocation(geoLocation: GeoLocation): Resource<Unit>

    suspend fun deleteCacheGeoLocation(geoLocation: GeoLocation): Resource<Unit>

    suspend fun isCached(latitude: Double, longitude: Double): Resource<Boolean>
}