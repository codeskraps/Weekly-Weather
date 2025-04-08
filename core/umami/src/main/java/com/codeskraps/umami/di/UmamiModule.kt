package com.codeskraps.umami.di

import android.app.Application
import com.codeskraps.umami.data.remote.UmamiAnalyticsDataSource
import com.codeskraps.umami.data.remote.UmamiConfig
import com.codeskraps.umami.data.repository.AnalyticsRepositoryImpl
import com.codeskraps.umami.data.repository.DeviceIdRepositoryImpl
import com.codeskraps.umami.domain.AnalyticsRepository
import com.codeskraps.umami.domain.DeviceIdRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreUmamiModule {

    @Provides
    @Singleton
    fun providesDeviceIdRepository(
        app: Application
    ): DeviceIdRepository {
        return DeviceIdRepositoryImpl(app)
    }

    @Provides
    @Singleton
    fun providesAnalyticsRepository(
        app: Application,
        deviceIdRepository: DeviceIdRepository
    ): AnalyticsRepository {
        return AnalyticsRepositoryImpl(
            UmamiAnalyticsDataSource(
                context = app,
                config = UmamiConfig(
                    scriptUrl = "https://umami.codeskraps.com/script.js",
                    websiteId = "047c76b3-6f42-45f0-ba9b-772b056ccdc1",
                    baseUrl = "https://umami.codeskraps.com"
                )
            ),
            deviceIdRepository = deviceIdRepository
        )
    }
}