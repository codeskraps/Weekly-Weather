package com.codeskraps.feature.radar.data.remote

import retrofit2.http.GET

interface RainViewerApi {
    @GET("public/weather-maps.json")
    suspend fun getRadarFrames(): RainViewerDto
}
