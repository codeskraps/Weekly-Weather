package com.trifork.feature.common.data.repository

import android.util.Log
import com.trifork.feature.common.data.local.GeocodingDB
import com.trifork.feature.common.data.mappers.toGeoLocationEntity
import com.trifork.feature.common.data.mappers.toGeocoding
import com.trifork.feature.common.domain.model.GeoLocation
import com.trifork.feature.common.domain.repository.LocalGeocodingRepository
import com.trifork.feature.common.util.Resource
import javax.inject.Inject

class LocalGeocodingRepositoryImpl @Inject constructor(
    private val geocodingDB: GeocodingDB
) : LocalGeocodingRepository {

    override suspend fun getCachedGeoLocation(): Resource<List<GeoLocation>> {
        return try {
            Resource.Success(
                data = geocodingDB.geocodingDao().getAll().map { it.toGeocoding() }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun saveCacheGeoLocation(geoLocation: GeoLocation): Resource<Unit> {
        return try {
            geocodingDB.geocodingDao().insert(geoLocation.toGeoLocationEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun deleteCacheGeoLocation(geoLocation: GeoLocation): Resource<Unit> {
        return try {
            geocodingDB.geocodingDao().delete(geoLocation.name)
            Resource.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun isCached(latitude: Double, longitude: Double): Resource<Boolean> {
        return try {
            Resource.Success(
                geocodingDB.geocodingDao().getByLocation(latitude, longitude).isNotEmpty()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}