package com.codeskraps.core.local.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codeskraps.core.local.data.model.GeoLocationEntity

@Database(
    entities = [GeoLocationEntity::class],
    version = 1
)
abstract class GeocodingDB: RoomDatabase() {

    abstract fun geocodingDao(): com.codeskraps.core.local.data.db.GeocodingDao
}