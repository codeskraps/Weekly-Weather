package com.codeskraps.maps.data.remote.wind

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoWindApi {
    @GET("v1/forecast")
    suspend fun getWindData(
        @Query("latitude") latitudes: String,
        @Query("longitude") longitudes: String,
        @Query("current") current: String = "wind_speed_10m,wind_direction_10m",
        @Query("forecast_days") forecastDays: Int = 1,
        @Query("timezone") timezone: String = "auto",
        @Query("wind_speed_unit") windSpeedUnit: String = "ms"
    ): List<WindLocationDto>
}
