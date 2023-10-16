package com.example.weather.di

import com.example.weather.feature.common.dispatcher.DispatcherProvider
import com.example.weather.feature.common.dispatcher.StandardDispatcherProvider
import com.example.weather.feature.weather.data.remote.WeatherApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDispatcherProvider(): DispatcherProvider {
        return StandardDispatcherProvider()
    }
}
