package com.codeskraps.feature.geocoding.repository

import com.codeskraps.core.local.domain.model.GeoLocation
import com.codeskraps.feature.common.util.Resource

interface GeocodingRepository {

    suspend fun getGeoLocation(query: String): Resource<List<com.codeskraps.core.local.domain.model.GeoLocation>>
}