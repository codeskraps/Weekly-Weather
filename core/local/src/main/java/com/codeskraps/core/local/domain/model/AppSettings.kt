package com.codeskraps.core.local.domain.model

data class AppSettings(
    val unitSystem: UnitSystem = UnitSystem.METRIC,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val radarSpeed: RadarSpeed = RadarSpeed.NORMAL,
)

enum class UnitSystem { METRIC, IMPERIAL }

enum class ThemeMode { SYSTEM, LIGHT, DARK }

enum class RadarSpeed { NORMAL, FAST }
