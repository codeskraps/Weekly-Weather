package com.codeskraps.maps.data.repository

import com.codeskraps.core.local.domain.repository.LocalResourceRepository
import com.codeskraps.feature.common.util.Resource
import com.codeskraps.maps.data.mappers.toRadarData
import com.codeskraps.maps.data.remote.RainViewerApi
import com.codeskraps.maps.domain.model.RadarData
import com.codeskraps.maps.domain.repository.RadarRepository

class RadarRepositoryImpl(
    private val api: RainViewerApi,
    private val localResource: LocalResourceRepository
) : RadarRepository {

    override suspend fun getRadarFrames(): Resource<RadarData> {
        return try {
            Resource.Success(api.getRadarFrames().toRadarData())
        } catch (e: Exception) {
            Resource.Error(localResource.getCheckInternetString())
        }
    }
}
