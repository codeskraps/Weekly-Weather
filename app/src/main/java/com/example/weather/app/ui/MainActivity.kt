package com.example.weather.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weather.app.navigation.Screen
import com.example.weather.app.ui.theme.WeatherTheme
import com.example.weather.feature.geocoding.presentation.GeocodingViewModel
import com.example.weather.feature.geocoding.presentation.components.GeocodingScreen
import com.example.weather.feature.weather.domain.model.WeatherLocation
import com.example.weather.feature.weather.presentation.WeatherViewModel
import com.example.weather.feature.weather.presentation.components.WeatherScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Screen.Weather.route) {
                    composable(
                        Screen.Weather.route,
                        arguments = listOf(
                            navArgument("name") { type = NavType.StringType },
                            navArgument("lat") { type = NavType.FloatType },
                            navArgument("long") { type = NavType.FloatType }
                        )
                    ) { backStackEntry ->
                        val name = backStackEntry.arguments?.getString("name")
                        val lat = backStackEntry.arguments?.getFloat("lat")
                        val long = backStackEntry.arguments?.getFloat("long")

                        val geoLocation = if (name.isNullOrBlank()) {
                            WeatherLocation()
                        } else {
                            WeatherLocation(
                                name = name,
                                lat = lat!!.toDouble(),
                                long = long!!.toDouble()
                            )
                        }
                        val viewModel by viewModels<WeatherViewModel>()
                        WeatherScreen(navController, viewModel, geoLocation)
                    }
                    composable(Screen.Geocoding.route) {
                        val viewModel by viewModels<GeocodingViewModel>()
                        GeocodingScreen(navController, viewModel)
                    }
                }
            }
        }
    }
}