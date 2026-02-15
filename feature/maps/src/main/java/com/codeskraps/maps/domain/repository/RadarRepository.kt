package com.codeskraps.maps.domain.repository

import com.codeskraps.feature.common.util.Resource
import com.codeskraps.maps.domain.model.RadarData

interface RadarRepository {
    suspend fun getRadarFrames(): Resource<RadarData>
}
