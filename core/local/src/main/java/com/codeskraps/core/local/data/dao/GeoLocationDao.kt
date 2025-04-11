package com.codeskraps.core.local.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codeskraps.core.local.data.model.GeoLocationEntity

@Dao
interface GeoLocationDao {
    @Query("SELECT * FROM GeoLocationEntity")
    suspend fun getAll(): List<GeoLocationEntity>

    @Query("SELECT * FROM GeoLocationEntity WHERE latitude = :latitude AND longitude = :longitude")
    suspend fun getByLocation(latitude: Double, longitude: Double): List<GeoLocationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(geoLocation: GeoLocationEntity)

    @Delete
    suspend fun delete(geoLocation: GeoLocationEntity)

    @Query("DELETE FROM GeoLocationEntity WHERE latitude = :latitude AND longitude = :longitude")
    suspend fun delete(latitude: Double, longitude: Double)
} 