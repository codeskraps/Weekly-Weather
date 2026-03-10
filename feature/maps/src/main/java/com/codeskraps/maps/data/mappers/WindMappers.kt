package com.codeskraps.maps.data.mappers

import com.codeskraps.maps.data.remote.wind.WindLocationDto
import com.codeskraps.maps.domain.model.WindData
import com.codeskraps.maps.domain.model.WindPoint

fun List<WindLocationDto>.toWindData(): WindData {
    return WindData(
        points = map { dto ->
            WindPoint(
                latitude = dto.latitude,
                longitude = dto.longitude,
                speedMs = dto.current.windSpeed10m,
                directionDeg = dto.current.windDirection10m
            )
        }
    )
}
