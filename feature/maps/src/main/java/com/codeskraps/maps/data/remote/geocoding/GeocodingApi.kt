package com.codeskraps.maps.data.remote.geocoding

import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApi {

    @GET("v1/search")
    suspend fun getGeoLocation(
        @Query("name") name: String
    ): GeocodingDto?
}
