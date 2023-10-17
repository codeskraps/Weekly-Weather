package com.trifork.feature.geocoding.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApi {

    @GET("v1/search")
    suspend fun getGeoLocation(
        @Query("name") name: String
    ): GeocodingDto?
}