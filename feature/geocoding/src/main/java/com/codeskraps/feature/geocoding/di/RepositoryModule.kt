package com.codeskraps.feature.geocoding.di

import com.codeskraps.feature.geocoding.data.repository.GeocodingRepositoryImpl
import com.codeskraps.feature.geocoding.repository.GeocodingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsGeocodingRepository(
        geocodingRepositoryImpl: GeocodingRepositoryImpl
    ): GeocodingRepository
}