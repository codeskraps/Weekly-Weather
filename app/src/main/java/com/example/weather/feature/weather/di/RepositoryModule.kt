package com.example.weather.feature.weather.di

import com.example.weather.feature.weather.data.repository.WeatherRepositoryImpl
import com.example.weather.feature.weather.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.internal.lifecycle.HiltViewModelMap
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsWeatherRepository(
        weatherRepositoryIml: WeatherRepositoryImpl
    ): WeatherRepository
}