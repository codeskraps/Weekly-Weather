package com.codeskraps.feature.common.navigation

sealed class Screen(val route: String) {
    data object Weather : Screen("weather/{name}/{lat}/{long}") {
        fun createRoute(name: String, lat: Double, long: Double): String {
            return "weather/$name/${lat.toBigDecimal().toPlainString()}/${
                long.toBigDecimal().toPlainString()
            }"
        }
    }

    data object Geocoding : Screen("geocoding")
    data object Map : Screen("map")
    data object Settings : Screen("settings")
    data object Radar : Screen("radar")
}
