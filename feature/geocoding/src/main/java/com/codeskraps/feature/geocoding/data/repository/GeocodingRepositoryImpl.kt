package com.codeskraps.feature.geocoding.data.repository

import com.codeskraps.core.local.domain.model.GeoLocation
import com.codeskraps.feature.common.util.Resource
import com.codeskraps.feature.geocoding.data.mappers.toGeocoding
import com.codeskraps.feature.geocoding.data.remote.GeocodingApi
import com.codeskraps.feature.geocoding.repository.GeocodingRepository

class GeocodingRepositoryImpl(
    private val api: GeocodingApi,
) : GeocodingRepository {

    override suspend fun getGeoLocation(query: String): Resource<List<GeoLocation>> {
        return try {
            if (query.length < 3) throw Exception("Not enough length")
            val data = api.getGeoLocation(query.trim())
            if (data != null && data.results.isNotEmpty()) {
                Resource.Success(
                    data = data.toGeocoding()
                )
            } else {
                Resource.Error("No query match")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}