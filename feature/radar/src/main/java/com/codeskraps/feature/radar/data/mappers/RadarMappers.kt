package com.codeskraps.feature.radar.data.mappers

import com.codeskraps.feature.radar.data.remote.RadarFrameDto
import com.codeskraps.feature.radar.data.remote.RainViewerDto
import com.codeskraps.feature.radar.domain.model.RadarData
import com.codeskraps.feature.radar.domain.model.RadarFrame

fun RainViewerDto.toRadarData(): RadarData {
    return RadarData(
        past = radar.past.map { it.toRadarFrame(host) },
        nowcast = radar.nowcast.map { it.toRadarFrame(host) }
    )
}

private fun RadarFrameDto.toRadarFrame(host: String): RadarFrame {
    return RadarFrame(
        timestamp = time,
        tileUrl = "$host$path/256/{z}/{x}/{y}/2/1_1.png"
    )
}
