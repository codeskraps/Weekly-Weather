package com.codeskraps.umami.di

import android.app.Application
import com.codeskraps.umami.data.repository.AnalyticsRepositoryImpl
import com.codeskraps.umami.data.repository.DeviceIdRepositoryImpl
import com.codeskraps.umami.domain.AnalyticsRepository
import com.codeskraps.umami.domain.DeviceIdRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val umamiModule = module {
    single<DeviceIdRepository> { DeviceIdRepositoryImpl(androidApplication()) }
    single<AnalyticsRepository> { AnalyticsRepositoryImpl(androidApplication(), get()) }
}