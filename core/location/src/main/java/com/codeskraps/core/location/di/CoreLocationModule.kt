package com.codeskraps.core.location.di

import android.app.Application
import com.codeskraps.core.location.data.DefaultLocationTracker
import com.codeskraps.core.location.domain.LocationTracker
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val locationModule = module {
    single<LocationTracker> { provideLocationTracker(androidApplication()) }
}

private fun provideLocationTracker(app: Application): LocationTracker {
    return DefaultLocationTracker(
        locationClient = LocationServices.getFusedLocationProviderClient(app),
        application = app
    )
}