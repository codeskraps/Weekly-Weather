package com.codeskraps.core.local.data.repository

import com.codeskraps.core.local.domain.model.GeoLocation
import com.codeskraps.core.local.domain.repository.LocalGeocodingRepository
import com.codeskraps.core.local.data.mappers.toGeoLocationEntity
import com.codeskraps.core.local.data.mappers.toGeocoding
import com.codeskraps.core.local.domain.repository.LocalResourceRepository
import com.codeskraps.feature.common.util.Resource
import javax.inject.Inject

class LocalGeocodingRepositoryImpl @Inject constructor(
    private val geocodingDB: com.codeskraps.core.local.data.db.GeocodingDB,
    private val localResource: LocalResourceRepository
) : LocalGeocodingRepository {

    override suspend fun getCachedGeoLocation(): Resource<List<GeoLocation>> {
        return try {
            Resource.Success(
                data = geocodingDB.geocodingDao().getAll().map { it.toGeocoding() }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: localResource.getUnknownErrorString())
        }
    }

    override suspend fun saveCacheGeoLocation(geoLocation: GeoLocation): Resource<Unit> {
        return try {
            geocodingDB.geocodingDao().insert(geoLocation.toGeoLocationEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: localResource.getUnknownErrorString())
        }
    }

    override suspend fun deleteCacheGeoLocation(geoLocation: GeoLocation): Resource<Unit> {
        return try {
            geocodingDB.geocodingDao().delete(geoLocation.latitude, geoLocation.longitude)
            Resource.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: localResource.getUnknownErrorString())
        }
    }

    override suspend fun isCached(latitude: Double, longitude: Double): Resource<Boolean> {
        return try {
            Resource.Success(
                geocodingDB.geocodingDao().getByLocation(latitude, longitude).isNotEmpty()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: localResource.getUnknownErrorString())
        }
    }
}