package com.codeskraps.core.local.domain.repository

import com.codeskraps.core.local.domain.model.AppSettings
import com.codeskraps.core.local.domain.model.ThemeMode
import com.codeskraps.core.local.domain.model.UnitSystem
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<AppSettings>
    suspend fun setUnitSystem(unitSystem: UnitSystem)
    suspend fun setThemeMode(themeMode: ThemeMode)
    suspend fun getMapZoom(): Float
    suspend fun setMapZoom(zoom: Float)

    companion object {
        const val DEFAULT_MAP_ZOOM = 5f
    }
}
