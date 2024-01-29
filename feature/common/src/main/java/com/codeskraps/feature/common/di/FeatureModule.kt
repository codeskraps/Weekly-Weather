package com.codeskraps.feature.common.di

import android.app.Application
import android.content.res.Resources
import com.codeskraps.feature.common.dispatcher.DispatcherProvider
import com.codeskraps.feature.common.dispatcher.StandardDispatcherProvider
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
    fun providesResources(
        application: Application
    ): Resources {
        return application.resources
    }
}
