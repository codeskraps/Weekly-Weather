package com.example.weather.feature.weather.di

import com.example.weather.feature.weather.data.location.DefaultLocationTracker
import com.example.weather.feature.weather.domain.location.LocationTracker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class LocationModule {

    @Binds
    abstract fun bindsLocationTracker(
        defaultLocationTracker: DefaultLocationTracker
    ): LocationTracker
}