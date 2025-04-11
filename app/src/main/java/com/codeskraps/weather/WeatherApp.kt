package com.codeskraps.weather

import android.app.Application
import com.codeskraps.core.local.di.localModule
import com.codeskraps.core.location.di.locationModule
import com.codeskraps.feature.common.di.commonModule
import com.codeskraps.feature.geocoding.di.geocodingModule
import com.codeskraps.feature.weather.di.weatherModule
import com.codeskraps.maps.di.mapsFeatureModule
import com.codeskraps.umami.di.umamiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WeatherApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@WeatherApp)
            modules(
                commonModule,
                localModule,
                locationModule,
                geocodingModule,
                weatherModule,
                mapsFeatureModule,
                umamiModule
            )
        }
    }
}