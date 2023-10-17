package com.trifork.feature.weather.di

import com.trifork.feature.weather.data.repository.WeatherRepositoryImpl
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
    ): com.trifork.feature.weather.domain.repository.WeatherRepository
}