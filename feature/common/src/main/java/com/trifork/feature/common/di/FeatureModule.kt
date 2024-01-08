package com.trifork.feature.common.di

import android.app.Application
import androidx.room.Room
import com.trifork.feature.common.data.local.GeocodingDB
import com.trifork.feature.common.dispatcher.DispatcherProvider
import com.trifork.feature.common.dispatcher.StandardDispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FeatureModule {

    @Provides
    @Singleton
    fun providesDispatcherProvider(): DispatcherProvider {
        return StandardDispatcherProvider()
    }

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
