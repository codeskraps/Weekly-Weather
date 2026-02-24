package com.codeskraps.maps.di

import com.codeskraps.maps.data.remote.RainViewerApi
import com.codeskraps.maps.data.remote.geocoding.GeocodingApi
import com.codeskraps.maps.data.repository.GeocodingRepositoryImpl
import com.codeskraps.maps.data.repository.RadarRepositoryImpl
import com.codeskraps.maps.domain.repository.GeocodingRepository
import com.codeskraps.maps.domain.repository.RadarRepository
import com.codeskraps.maps.presentation.MapViewModel
import com.codeskraps.maps.presentation.RadarViewModel
import com.squareup.moshi.Moshi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

val mapsFeatureModule = module {
    // Geocoding (for map search)
    single { provideGeocodingApi() }
    single<GeocodingRepository> { GeocodingRepositoryImpl(get()) }

    // Map
    viewModel {
        MapViewModel(
            locationTracker = get(),
            localGeocodingRepository = get(),
            geocodingRepository = get(),
            localResources = get(),
            dispatcherProvider = get(),
            analytics = get(),
            activeLocationRepository = get(),
            settingsRepository = get()
        )
    }

    // Radar
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
            analyticsRepository = get(),
            settingsRepository = get()
        )
    }
}

private fun provideGeocodingApi(): GeocodingApi {
    val moshi = Moshi.Builder().build()

    return Retrofit.Builder()
        .baseUrl("https://geocoding-api.open-meteo.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create()
}

private fun provideRainViewerApi(): RainViewerApi {
    val moshi = Moshi.Builder().build()

    return Retrofit.Builder()
        .baseUrl("https://api.rainviewer.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create()
}
