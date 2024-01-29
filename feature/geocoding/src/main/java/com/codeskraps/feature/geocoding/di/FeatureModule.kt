package com.codeskraps.feature.geocoding.di

import com.codeskraps.feature.geocoding.data.remote.GeocodingApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

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
}