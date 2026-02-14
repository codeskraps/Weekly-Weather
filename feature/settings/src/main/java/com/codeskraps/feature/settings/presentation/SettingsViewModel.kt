package com.codeskraps.feature.settings.presentation

import androidx.lifecycle.viewModelScope
import com.codeskraps.core.local.domain.model.ThemeMode
import com.codeskraps.core.local.domain.model.UnitSystem
import com.codeskraps.core.local.domain.repository.SettingsRepository
import com.codeskraps.feature.common.dispatcher.DispatcherProvider
import com.codeskraps.feature.common.mvi.StateReducerViewModel
import com.codeskraps.feature.settings.presentation.mvi.SettingsAction
import com.codeskraps.feature.settings.presentation.mvi.SettingsEvent
import com.codeskraps.feature.settings.presentation.mvi.SettingsState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val dispatcherProvider: DispatcherProvider,
) : StateReducerViewModel<SettingsState, SettingsEvent, SettingsAction>(SettingsState.initial) {

    override fun reduceState(
        currentState: SettingsState,
        event: SettingsEvent
    ): SettingsState {
        return when (event) {
            is SettingsEvent.Resume -> onResume(currentState)
            is SettingsEvent.SetUnitSystem -> onSetUnitSystem(currentState, event.unitSystem)
            is SettingsEvent.SetThemeMode -> onSetThemeMode(currentState, event.themeMode)
            is SettingsEvent.Loaded -> currentState.copy(
                unitSystem = event.unitSystem,
                themeMode = event.themeMode,
            )
        }
    }

    private fun onResume(currentState: SettingsState): SettingsState {
        viewModelScope.launch(dispatcherProvider.io) {
            val settings = settingsRepository.settings.first()
            state.handleEvent(
                SettingsEvent.Loaded(
                    unitSystem = settings.unitSystem,
                    themeMode = settings.themeMode,
                )
            )
        }
        return currentState
    }

    private fun onSetUnitSystem(currentState: SettingsState, unitSystem: UnitSystem): SettingsState {
        viewModelScope.launch(dispatcherProvider.io) {
            settingsRepository.setUnitSystem(unitSystem)
        }
        return currentState.copy(unitSystem = unitSystem)
    }

    private fun onSetThemeMode(currentState: SettingsState, themeMode: ThemeMode): SettingsState {
        viewModelScope.launch(dispatcherProvider.io) {
            settingsRepository.setThemeMode(themeMode)
        }
        return currentState.copy(themeMode = themeMode)
    }
}
