package com.codeskraps.feature.settings.presentation.mvi

import com.codeskraps.core.local.domain.model.RadarSpeed
import com.codeskraps.core.local.domain.model.ThemeMode
import com.codeskraps.core.local.domain.model.UnitSystem

data class SettingsState(
    val unitSystem: UnitSystem,
    val themeMode: ThemeMode,
    val radarSpeed: RadarSpeed,
) {
    companion object {
        val initial = SettingsState(
            unitSystem = UnitSystem.METRIC,
            themeMode = ThemeMode.SYSTEM,
            radarSpeed = RadarSpeed.NORMAL,
        )
    }
}
