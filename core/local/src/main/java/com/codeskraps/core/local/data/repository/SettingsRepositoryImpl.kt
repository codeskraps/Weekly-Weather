package com.codeskraps.core.local.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.codeskraps.core.local.domain.model.AppSettings
import com.codeskraps.core.local.domain.model.ThemeMode
import com.codeskraps.core.local.domain.model.UnitSystem
import com.codeskraps.core.local.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepositoryImpl(
    private val context: Context
) : SettingsRepository {

    private object Keys {
        val UNIT_SYSTEM = stringPreferencesKey("unit_system")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val MAP_ZOOM = floatPreferencesKey("map_zoom")
    }

    override val settings: Flow<AppSettings> = context.dataStore.data.map { prefs ->
        AppSettings(
            unitSystem = prefs[Keys.UNIT_SYSTEM]?.let { UnitSystem.valueOf(it) }
                ?: UnitSystem.METRIC,
            themeMode = prefs[Keys.THEME_MODE]?.let { ThemeMode.valueOf(it) }
                ?: ThemeMode.SYSTEM,
        )
    }

    override suspend fun setUnitSystem(unitSystem: UnitSystem) {
        context.dataStore.edit { prefs ->
            prefs[Keys.UNIT_SYSTEM] = unitSystem.name
        }
    }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        context.dataStore.edit { prefs ->
            prefs[Keys.THEME_MODE] = themeMode.name
        }
    }

    override suspend fun getMapZoom(): Float {
        return context.dataStore.data.first()[Keys.MAP_ZOOM] ?: SettingsRepository.DEFAULT_MAP_ZOOM
    }

    override suspend fun setMapZoom(zoom: Float) {
        context.dataStore.edit { prefs ->
            prefs[Keys.MAP_ZOOM] = zoom
        }
    }
}
