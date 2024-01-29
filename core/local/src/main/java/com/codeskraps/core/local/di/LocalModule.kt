package com.codeskraps.core.local.di

import android.app.Application
import androidx.room.Room
import com.codeskraps.core.local.data.db.GeocodingDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun providesGeocodingDB(
        application: Application
    ): GeocodingDB {
        return Room.databaseBuilder(
            application,
            GeocodingDB::class.java, "database-name"
        ).build()
    }
}