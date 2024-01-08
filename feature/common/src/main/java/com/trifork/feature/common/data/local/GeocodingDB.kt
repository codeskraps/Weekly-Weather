package com.trifork.feature.common.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [GeoLocationEntity::class],
    version = 1
)
abstract class GeocodingDB: RoomDatabase() {

    abstract fun geocodingDao(): GeocodingDao
}