package com.trifork.feature.common.di

import com.trifork.feature.common.data.repository.LocalGeocodingRepositoryImpl
import com.trifork.feature.common.domain.repository.LocalGeocodingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsGeocodingRepository(
        localGeocodingRepositoryImpl: LocalGeocodingRepositoryImpl
    ): LocalGeocodingRepository
}