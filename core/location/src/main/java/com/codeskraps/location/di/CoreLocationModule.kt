package com.codeskraps.location.di

import android.app.Application
import com.codeskraps.location.data.DefaultLocationTracker
import com.codeskraps.location.domain.LocationTracker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
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