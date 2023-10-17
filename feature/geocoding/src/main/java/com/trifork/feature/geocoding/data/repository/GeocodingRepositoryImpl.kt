package com.trifork.feature.geocoding.data.repository

import com.trifork.feature.common.util.Resource
import com.trifork.feature.geocoding.data.local.GeocodingDB
import com.trifork.feature.geocoding.data.mappers.toGeoLocationEntity
import com.trifork.feature.geocoding.data.mappers.toGeocoding
import com.trifork.feature.geocoding.data.remote.GeocodingApi
import com.trifork.feature.geocoding.domain.model.GeoLocation
import com.trifork.feature.geocoding.domain.repository.GeocodingRepository
import javax.inject.Inject

class GeocodingRepositoryImpl @Inject constructor(
    private val api: GeocodingApi,
    private val db: GeocodingDB
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

    override suspend fun getCachedGeoLocation(): Resource<List<GeoLocation>> {
        return try {
            Resource.Success(
                data = db.geocodingDao().getAll().map { it.toGeocoding() }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun saveCacheGeoLocation(geoLocation: GeoLocation): Resource<Unit> {
        return try {
            db.geocodingDao().insert(geoLocation.toGeoLocationEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun deleteCacheGeoLocation(geoLocation: GeoLocation): Resource<Unit> {
        return try {
            db.geocodingDao().delete(geoLocation.toGeoLocationEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}