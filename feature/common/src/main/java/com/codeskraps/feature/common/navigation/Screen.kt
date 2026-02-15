package com.codeskraps.feature.common.navigation

sealed class Screen(val route: String) {
    data object Weather : Screen("weather")
    data object Map : Screen("map")
    data object Settings : Screen("settings")
}
