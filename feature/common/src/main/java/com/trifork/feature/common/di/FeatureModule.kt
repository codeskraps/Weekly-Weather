package com.trifork.feature.common.di

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
}
