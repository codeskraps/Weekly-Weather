package com.codeskraps.feature.common.di

import android.app.Application
import android.content.res.Resources
import com.codeskraps.feature.common.data.repository.ActiveLocationRepositoryImpl
import com.codeskraps.feature.common.dispatcher.DispatcherProvider
import com.codeskraps.feature.common.dispatcher.StandardDispatcherProvider
import com.codeskraps.feature.common.domain.repository.ActiveLocationRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val commonModule = module {
    single<DispatcherProvider> { StandardDispatcherProvider() }
    single<ActiveLocationRepository> { ActiveLocationRepositoryImpl() }
    single { provideResources(androidApplication()) }
}

private fun provideResources(application: Application): Resources {
    return application.resources
}
