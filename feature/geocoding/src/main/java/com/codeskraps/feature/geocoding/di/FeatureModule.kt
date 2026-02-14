package com.codeskraps.feature.geocoding.di

import com.codeskraps.feature.geocoding.data.remote.GeocodingApi
import com.codeskraps.feature.geocoding.data.repository.GeocodingRepositoryImpl
import com.codeskraps.feature.geocoding.presentation.GeocodingViewModel
import com.codeskraps.feature.geocoding.repository.GeocodingRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

val geocodingModule = module {
    single { provideGeocodingApi() }
    single<GeocodingRepository> { GeocodingRepositoryImpl(get()) }
    viewModel { GeocodingViewModel(get(), get(), get(), get(), get()) }
}

private fun provideGeocodingApi(): GeocodingApi {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    return Retrofit.Builder()
        .baseUrl("https://geocoding-api.open-meteo.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create()
}