package com.example.weather.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weather.app.ui.theme.WeatherTheme
import com.trifork.feature.common.navigation.Screen
import com.trifork.feature.geocoding.presentation.GeocodingViewModel
import com.trifork.feature.geocoding.presentation.components.GeocodingScreen
import com.trifork.feature.weather.presentation.WeatherViewModel
import com.trifork.feature.weather.presentation.components.WeatherScreen
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
                            navArgument("lat") { type = NavType.StringType },
                            navArgument("long") { type = NavType.StringType }
                        )
                    ) {

                        val viewModel = hiltViewModel<WeatherViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        WeatherScreen(
                            navController = navController,
                            state = state,
                            handleEvent = viewModel.state::handleEvent,
                            action = viewModel.action
                        )
                    }
                    composable(Screen.Geocoding.route) {
                        val viewModel = hiltViewModel<GeocodingViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        GeocodingScreen(
                            navController = navController,
                            state = state,
                            handleEvent = viewModel.state::handleEvent,
                            action = viewModel.action
                        )
                    }
                }
            }
        }
    }
}