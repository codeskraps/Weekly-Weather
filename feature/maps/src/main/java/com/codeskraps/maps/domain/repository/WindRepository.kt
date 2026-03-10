package com.codeskraps.maps.domain.repository

import com.codeskraps.feature.common.util.Resource
import com.codeskraps.maps.domain.model.WindData

interface WindRepository {
    suspend fun getWindGrid(
        latMin: Double,
        latMax: Double,
        lngMin: Double,
        lngMax: Double,
        gridSize: Int = 5
    ): Resource<WindData>
}
