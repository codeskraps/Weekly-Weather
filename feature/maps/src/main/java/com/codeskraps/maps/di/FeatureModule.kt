package com.codeskraps.maps.di

import com.codeskraps.maps.presentation.MapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mapsFeatureModule = module {
    viewModel {
        MapViewModel(
            locationTracker = get(),
            localGeocodingRepository = get(),
            localResources = get(),
            dispatcherProvider = get(),
            analytics = get()
        )
    }
} 