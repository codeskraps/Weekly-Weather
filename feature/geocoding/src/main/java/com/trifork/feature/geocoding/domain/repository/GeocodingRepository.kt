package com.trifork.feature.geocoding.domain.repository

import com.trifork.feature.common.domain.model.GeoLocation
import com.trifork.feature.common.util.Resource

interface GeocodingRepository {

    suspend fun getGeoLocation(query: String): Resource<List<GeoLocation>>
}