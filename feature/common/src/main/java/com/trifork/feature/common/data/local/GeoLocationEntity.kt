package com.trifork.feature.common.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GeoLocationEntity(
    @PrimaryKey
    val uid: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val admin1: String?
)