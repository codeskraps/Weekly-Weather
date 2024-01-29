package com.codeskraps.core.local.di

import com.codeskraps.core.local.data.repository.LocalGeocodingRepositoryImpl
import com.codeskraps.core.local.data.repository.LocalResourceRepositoryImpl
import com.codeskraps.core.local.domain.repository.LocalGeocodingRepository
import com.codeskraps.core.local.domain.repository.LocalResourceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class LocalRepositoryModule {

    @Binds
    abstract fun bindsGeocodingRepository(
        localGeocodingRepositoryImpl: LocalGeocodingRepositoryImpl
    ): LocalGeocodingRepository

    @Binds
    abstract fun bindsLocalResourceRepository(
        localResourceRepositoryImpl: LocalResourceRepositoryImpl
    ): LocalResourceRepository
}