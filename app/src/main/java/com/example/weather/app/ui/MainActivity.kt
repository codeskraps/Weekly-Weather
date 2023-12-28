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
import com.example.weather.app.ui.theme.WeatherTheme
import com.trifork.feature.common.navigation.Screen
import com.trifork.feature.geocoding.presentation.GeocodingViewModel
import com.trifork.feature.geocoding.presentation.components.GeocodingScreen
import com.trifork.feature.weather.domain.model.WeatherLocation
import com.trifork.feature.weather.presentation.WeatherViewModel
import com.trifork.feature.weather.presentation.components.WeatherScreen
import com.trifork.feature.weather.presentation.mvi.WeatherEvent
import com.trifork.feature.weather.presentation.mvi.WeatherState
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


                        val geoLocation = backStackEntry.arguments?.let {
                            val name = it.getString("name") ?: ""
                            val lat = it.getFloat("lat").toDouble()
                            val long = it.getFloat("long").toDouble()

                            it.remove("name")
                            it.remove("lat")
                            it.remove("long")

                            if (name.isNotBlank() && lat != .0 && long != .0) {
                                WeatherLocation(
                                    name = name,
                                    lat = lat,
                                    long = long
                                )
                            } else WeatherLocation()
                        } ?: WeatherLocation()

                        val viewModel by viewModels<WeatherViewModel>()

                        viewModel.state.handleEvent(WeatherEvent.LoadWeatherInfo(geoLocation))

                        WeatherScreen(
                            navController,
                            viewModel
                        )
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