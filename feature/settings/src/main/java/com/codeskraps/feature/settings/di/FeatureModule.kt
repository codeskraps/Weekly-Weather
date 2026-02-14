package com.codeskraps.feature.settings.di

import com.codeskraps.feature.settings.presentation.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    viewModel {
        SettingsViewModel(
            settingsRepository = get(),
            dispatcherProvider = get(),
        )
    }
}
