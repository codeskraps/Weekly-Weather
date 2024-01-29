package com.codeskraps.core.location.di

import android.app.Application
import com.codeskraps.core.location.data.DefaultLocationTracker
import com.codeskraps.core.location.domain.LocationTracker
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreLocationModule {

    @Provides
    @Singleton
    fun providesLocationTracker(
        app: Application
    ): LocationTracker {
        return DefaultLocationTracker(
            locationClient = LocationServices.getFusedLocationProviderClient(app),
            application = app
        )
    }
}