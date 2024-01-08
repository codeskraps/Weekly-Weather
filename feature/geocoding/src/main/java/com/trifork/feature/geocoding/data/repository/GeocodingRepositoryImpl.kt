package com.trifork.feature.geocoding.data.repository

import com.trifork.feature.common.domain.model.GeoLocation
import com.trifork.feature.common.util.Resource
import com.trifork.feature.geocoding.data.mappers.toGeocoding
import com.trifork.feature.geocoding.data.remote.GeocodingApi
import com.trifork.feature.geocoding.domain.repository.GeocodingRepository
import javax.inject.Inject

class GeocodingRepositoryImpl @Inject constructor(
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