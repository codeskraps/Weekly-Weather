package com.codeskraps.weather.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.codeskraps.core.local.domain.model.ThemeMode

private val lightColors = lightColorScheme(
    primary = md_theme_light_primary,
    secondary = md_theme_light_secondary,
    primaryContainer = md_theme_light_primaryContainer,
    background = md_theme_light_background,
    surface = md_theme_light_surface,
    outline = md_theme_light_outline
)
private val darkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    secondary = md_theme_dark_secondary,
    primaryContainer = md_theme_dark_primaryContainer,
    background = md_theme_dark_background,
    surface = md_theme_dark_surface,
    outline = md_theme_dark_outline
)

@Composable
fun WeatherTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val useDarkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    val colors = if (!useDarkTheme) {
        lightColors
    } else {
        darkColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}