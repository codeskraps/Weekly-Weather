package com.codeskraps.feature.weather.di

import com.codeskraps.feature.weather.data.repository.WeatherRepositoryImpl
import com.codeskraps.feature.weather.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsWeatherRepository(
        weatherRepositoryIml: WeatherRepositoryImpl
    ): WeatherRepository
}