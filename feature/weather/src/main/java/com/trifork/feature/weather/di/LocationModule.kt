package com.trifork.feature.weather.di

import com.trifork.feature.weather.domain.location.LocationTracker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class LocationModule {

    @Binds
    abstract fun bindsLocationTracker(
        defaultLocationTracker: com.trifork.feature.weather.data.location.DefaultLocationTracker
    ): LocationTracker
}