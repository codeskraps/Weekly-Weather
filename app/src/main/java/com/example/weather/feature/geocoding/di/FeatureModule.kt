package com.example.weather.feature.geocoding.di

import android.app.Application
import androidx.room.Room
import com.example.weather.feature.geocoding.data.local.GeocodingDB
import com.example.weather.feature.geocoding.data.remote.GeocodingApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object FeatureModule {

    @Provides
    fun providesGeocodingApi(): GeocodingApi {
        return Retrofit.Builder()
            .baseUrl("https://geocoding-api.open-meteo.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    fun providesGeocodingDB(
        application: Application
    ): GeocodingDB {
        return Room.databaseBuilder(
            application,
            GeocodingDB::class.java, "database-name"
        ).build()
    }
}