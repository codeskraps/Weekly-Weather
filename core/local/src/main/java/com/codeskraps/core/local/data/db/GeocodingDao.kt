package com.codeskraps.core.local.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface GeocodingDao {

    @Query("SELECT * FROM GeoLocationEntity")
    fun getAll(): List<GeoLocationEntity>

    @Upsert
    fun insert(geoLocationEntity: GeoLocationEntity)

    @Query("DELETE FROM GeoLocationEntity WHERE latitude = :latitude AND longitude = :longitude")
    fun delete(latitude: Double, longitude: Double)

    @Query("SELECT * FROM GeoLocationEntity WHERE latitude=:latitude AND longitude=:longitude")
    fun getByLocation(latitude: Double, longitude: Double): List<GeoLocationEntity>
}