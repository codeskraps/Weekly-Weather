package com.codeskraps.feature.weather.di

import com.codeskraps.feature.weather.data.remote.WeatherApi
import com.codeskraps.feature.weather.data.repository.WeatherRepositoryImpl
import com.codeskraps.feature.weather.domain.repository.WeatherRepository
import com.codeskraps.feature.weather.presentation.WeatherViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

val weatherModule = module {
    single { provideWeatherApi() }
    single<WeatherRepository> { 
        WeatherRepositoryImpl(
            api = get(),
            localResource = get()
        ) 
    }
    viewModel { 
        WeatherViewModel(
            localGeocodingRepository = get(),
            weatherRepository = get(),
            locationTracker = get(),
            localResource = get(),
            dispatcherProvider = get(),
            savedStateHandle = get(),
            analyticsRepository = get()
        ) 
    }
}

private fun provideWeatherApi(): WeatherApi {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    return Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create()
}