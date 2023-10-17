package com.trifork.feature.geocoding.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface GeocodingDao {

    @Query("SELECT * FROM GeoLocationEntity")
    fun getAll(): List<GeoLocationEntity>

    @Upsert
    fun insert(geoLocationEntity: GeoLocationEntity)

    @Delete
    fun delete(geoLocationEntity: GeoLocationEntity)
}