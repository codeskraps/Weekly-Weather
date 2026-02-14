package com.codeskraps.feature.radar.domain.repository

import com.codeskraps.feature.common.util.Resource
import com.codeskraps.feature.radar.domain.model.RadarData

interface RadarRepository {
    suspend fun getRadarFrames(): Resource<RadarData>
}
