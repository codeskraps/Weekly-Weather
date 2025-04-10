package com.codeskraps.core.local.di

import com.codeskraps.core.local.data.repository.LocalGeocodingRepositoryImpl
import com.codeskraps.core.local.data.repository.LocalResourceRepositoryImpl
import com.codeskraps.core.local.domain.repository.LocalGeocodingRepository
import com.codeskraps.core.local.domain.repository.LocalResourceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsGeocodingRepository(
        localGeocodingRepositoryImpl: LocalGeocodingRepositoryImpl
    ): LocalGeocodingRepository

    @Binds
    @Singleton
    abstract fun bindsLocalResourceRepository(
        localResourceRepositoryImpl: LocalResourceRepositoryImpl
    ): LocalResourceRepository
}