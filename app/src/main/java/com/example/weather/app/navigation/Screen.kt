package com.example.weather.app.navigation

sealed class Screen(val route: String) {
    data object Weather : Screen("weather/{name}/{lat}/{long}") {
        fun createRoute(name: String, lat: Double, long: Double): String {
            return "weather/$name/$lat/$long"
        }
    }

    data object Geocoding : Screen("geocoding")
}
