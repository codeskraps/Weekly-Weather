package com.trifork.feature.geocoding.domain.repository

import com.trifork.feature.common.util.Resource
import com.trifork.feature.geocoding.domain.model.GeoLocation

interface GeocodingRepository {

    suspend fun getGeoLocation(query: String): Resource<List<GeoLocation>>

    suspend fun getCachedGeoLocation(): Resource<List<GeoLocation>>

    suspend fun saveCacheGeoLocation(geoLocation: GeoLocation): Resource<Unit>

    suspend fun deleteCacheGeoLocation(geoLocation: GeoLocation): Resource<Unit>
}