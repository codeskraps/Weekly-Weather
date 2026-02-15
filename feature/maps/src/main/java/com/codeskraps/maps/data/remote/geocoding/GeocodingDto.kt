package com.codeskraps.maps.data.remote.geocoding

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeocodingDto(
    @Json(name = "results")
    val results: List<Result> = emptyList()
) {
    @JsonClass(generateAdapter = true)
    data class Result(
        @Json(name = "id")
        val id: Int,
        @Json(name = "name")
        val name: String,
        @Json(name = "latitude")
        val latitude: Double,
        @Json(name = "longitude")
        val longitude: Double,
        @Json(name = "elevation")
        val elevation: Double? = null,
        @Json(name = "feature_code")
        val featureCode: String? = null,
        @Json(name = "country_code")
        val countryCode: String? = null,
        @Json(name = "admin1_id")
        val admin1Id: Int? = null,
        @Json(name = "admin2_id")
        val admin2Id: Int? = null,
        @Json(name = "timezone")
        val timezone: String? = null,
        @Json(name = "population")
        val population: Int? = null,
        @Json(name = "country_id")
        val countryId: Int? = null,
        @Json(name = "country")
        val country: String? = null,
        @Json(name = "admin1")
        val admin1: String? = null,
        @Json(name = "admin2")
        val admin2: String? = null
    )
}
