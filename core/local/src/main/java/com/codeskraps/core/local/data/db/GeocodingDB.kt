package com.codeskraps.core.local.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.codeskraps.core.local.data.dao.GeoLocationDao
import com.codeskraps.core.local.data.model.GeoLocationEntity

@Database(
    entities = [GeoLocationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GeocodingDB : RoomDatabase() {
    abstract fun geocodingDao(): GeoLocationDao

    companion object {
        @Volatile
        private var INSTANCE: GeocodingDB? = null

        fun getInstance(context: Context): GeocodingDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GeocodingDB::class.java,
                    "geocoding_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}