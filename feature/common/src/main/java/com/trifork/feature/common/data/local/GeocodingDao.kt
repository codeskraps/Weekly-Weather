package com.trifork.feature.common.data.local

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

    @Query("DELETE FROM GeoLocationEntity WHERE name = :name")
    fun delete(name: String)

    @Query("SELECT * FROM GeoLocationEntity WHERE latitude=:latitude AND longitude=:longitude")
    fun getByLocation(latitude: Double, longitude: Double): List<GeoLocationEntity>
}