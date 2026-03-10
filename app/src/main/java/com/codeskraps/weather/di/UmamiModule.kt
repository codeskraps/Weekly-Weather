package com.codeskraps.weather.di

import com.codeskraps.umamilib.Umami
import com.codeskraps.umamilib.UmamiConfig
import com.codeskraps.umamilib.create
import com.codeskraps.umamilib.domain.UmamiAnalytics
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val umamiModule = module {
    single<UmamiAnalytics> {
        Umami.create(
            application = androidApplication(),
            config = UmamiConfig(
                websiteId = "333912f3-52e9-4841-a798-a20fe324e07a",
                baseUrl = "https://umami.codeskraps.com",
                hostname = "weather.app",
                appName = "WeeklyWeather"
            )
        )
    }
}
