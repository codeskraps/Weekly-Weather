package com.codeskraps.weather.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.codeskraps.core.local.domain.model.AppSettings
import com.codeskraps.core.local.domain.repository.SettingsRepository
import com.codeskraps.umamilib.domain.UmamiAnalytics
import com.codeskraps.weather.ui.theme.WeatherTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val analyticsRepository: UmamiAnalytics by inject()
    private val settingsRepository: SettingsRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize analytics in a coroutine
        lifecycleScope.launch {
            analyticsRepository.initialize()
        }

        setContent {
            val settings by settingsRepository.settings
                .collectAsStateWithLifecycle(initialValue = AppSettings())
            WeatherTheme(themeMode = settings.themeMode) {
                WeatherNavHost()
            }
        }
    }
}