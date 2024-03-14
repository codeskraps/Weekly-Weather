package com.codeskraps.core.local.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codeskraps.core.local.data.model.GeoLocationEntity

@Database(
    entities = [GeoLocationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GeocodingDB: RoomDatabase() {

    abstract fun geocodingDao(): GeocodingDao
}