package com.example.weather.feature.geocoding.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [GeoLocationEntity::class],
    version = 1
)
abstract class GeocodingDB: RoomDatabase() {

    abstract fun geocodingDao(): GeocodingDao
}