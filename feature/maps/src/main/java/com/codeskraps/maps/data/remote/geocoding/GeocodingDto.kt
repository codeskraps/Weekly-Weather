package com.codeskraps.maps.data.remote.geocoding

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeocodingDto(
    @param:Json(name = "results")
    val results: List<Result> = emptyList()
) {
    @JsonClass(generateAdapter = true)
    data class Result(
        @param:Json(name = "id")
        val id: Int,
        @param:Json(name = "name")
        val name: String,
        @param:Json(name = "latitude")
        val latitude: Double,
        @param:Json(name = "longitude")
        val longitude: Double,
        @param:Json(name = "elevation")
        val elevation: Double? = null,
        @param:Json(name = "feature_code")
        val featureCode: String? = null,
        @param:Json(name = "country_code")
        val countryCode: String? = null,
        @param:Json(name = "admin1_id")
        val admin1Id: Int? = null,
        @param:Json(name = "admin2_id")
        val admin2Id: Int? = null,
        @param:Json(name = "timezone")
        val timezone: String? = null,
        @param:Json(name = "population")
        val population: Int? = null,
        @param:Json(name = "country_id")
        val countryId: Int? = null,
        @param:Json(name = "country")
        val country: String? = null,
        @param:Json(name = "admin1")
        val admin1: String? = null,
        @param:Json(name = "admin2")
        val admin2: String? = null
    )
}
