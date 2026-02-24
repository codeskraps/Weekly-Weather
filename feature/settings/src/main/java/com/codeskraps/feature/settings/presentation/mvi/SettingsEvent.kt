package com.codeskraps.feature.settings.presentation.mvi

import com.codeskraps.core.local.domain.model.RadarSpeed
import com.codeskraps.core.local.domain.model.ThemeMode
import com.codeskraps.core.local.domain.model.UnitSystem

sealed interface SettingsEvent {
    data class SetUnitSystem(val unitSystem: UnitSystem) : SettingsEvent
    data class SetThemeMode(val themeMode: ThemeMode) : SettingsEvent
    data class SetRadarSpeed(val radarSpeed: RadarSpeed) : SettingsEvent
    data class Loaded(val unitSystem: UnitSystem, val themeMode: ThemeMode, val radarSpeed: RadarSpeed) : SettingsEvent
    data object Resume : SettingsEvent
}
