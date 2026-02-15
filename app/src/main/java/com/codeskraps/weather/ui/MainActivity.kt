package com.codeskraps.weather.ui

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.codeskraps.core.local.domain.model.AppSettings
import com.codeskraps.core.local.domain.repository.SettingsRepository
import com.codeskraps.umami.domain.AnalyticsRepository
import com.codeskraps.weather.ui.theme.WeatherTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val analyticsRepository: AnalyticsRepository by inject()
    private val settingsRepository: SettingsRepository by inject()

    private var isAnalyticsInitialized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Keep the splash screen visible until analytics initialization is complete
        splashScreen.setKeepOnScreenCondition { !isAnalyticsInitialized }

        // Initialize analytics in a coroutine
        lifecycleScope.launch {
            analyticsRepository.initialize()
            isAnalyticsInitialized = true
        }

        // Set up the OnPreDrawListener to the root view
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : android.view.ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check if analytics is initialized
                    return if (isAnalyticsInitialized) {
                        // The content is ready; remove the listener and start drawing
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        // The content isn't ready; suspend drawing
                        false
                    }
                }
            }
        )

        setContent {
            val settings by settingsRepository.settings
                .collectAsStateWithLifecycle(initialValue = AppSettings())
            WeatherTheme(themeMode = settings.themeMode) {
                WeatherNavHost()
            }
        }
    }
}