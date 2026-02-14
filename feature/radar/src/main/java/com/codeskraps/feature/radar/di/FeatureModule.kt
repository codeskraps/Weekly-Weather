package com.codeskraps.feature.radar.di

import com.codeskraps.feature.radar.data.remote.RainViewerApi
import com.codeskraps.feature.radar.data.repository.RadarRepositoryImpl
import com.codeskraps.feature.radar.domain.repository.RadarRepository
import com.codeskraps.feature.radar.presentation.RadarViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

val radarModule = module {
    single { provideRainViewerApi() }
    single<RadarRepository> {
        RadarRepositoryImpl(
            api = get(),
            localResource = get()
        )
    }
    viewModel {
        RadarViewModel(
            radarRepository = get(),
            locationTracker = get(),
            localResource = get(),
            dispatcherProvider = get(),
            analyticsRepository = get()
        )
    }
}

private fun provideRainViewerApi(): RainViewerApi {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    return Retrofit.Builder()
        .baseUrl("https://api.rainviewer.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create()
}
