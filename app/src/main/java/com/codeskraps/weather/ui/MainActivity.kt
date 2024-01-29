package com.codeskraps.weather.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.codeskraps.maps.presentation.MapViewModel
import com.codeskraps.maps.presentation.components.MapScreen
import com.codeskraps.weather.ui.theme.WeatherTheme
import com.codeskraps.feature.common.navigation.Screen
import com.codeskraps.feature.geocoding.presentation.GeocodingViewModel
import com.codeskraps.feature.geocoding.presentation.components.GeocodingScreen
import com.codeskraps.feature.weather.presentation.WeatherViewModel
import com.codeskraps.feature.weather.presentation.components.WeatherScreen
import com.codeskraps.weather.ui.theme.ScreenTransitions
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
                        route = Screen.Weather.route,
                        arguments = listOf(
                            navArgument("name") { type = NavType.StringType },
                            navArgument("lat") { type = NavType.StringType },
                            navArgument("long") { type = NavType.StringType }
                        ),
                        enterTransition = { ScreenTransitions.slideRightIntoContainer(this) },
                        exitTransition = { ScreenTransitions.slideLeftOutOfContainer(this) },
                        popEnterTransition = { ScreenTransitions.slideRightIntoContainer(this) },
                        popExitTransition = { ScreenTransitions.slideLeftOutOfContainer(this) }
                    ) {

                        val viewModel = hiltViewModel<WeatherViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        WeatherScreen(
                            state = state,
                            handleEvent = viewModel.state::handleEvent,
                            action = viewModel.action
                        ) { route ->
                            navController.navigate(route)
                        }
                    }
                    composable(
                        route = Screen.Geocoding.route,
                        enterTransition = { ScreenTransitions.slideLeftIntoContainer(this) },
                        exitTransition = { ScreenTransitions.slideRightOutOfContainer(this) },
                        popEnterTransition = { ScreenTransitions.slideLeftIntoContainer(this) },
                        popExitTransition = { ScreenTransitions.slideRightOutOfContainer(this) }
                    ) {
                        val viewModel = hiltViewModel<GeocodingViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        GeocodingScreen(
                            state = state,
                            handleEvent = viewModel.state::handleEvent,
                            action = viewModel.action,
                            navUp = { navController.navigateUp() }
                        ) { route ->
                            navController.navigate(route) {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                        }
                    }
                    composable(
                        route = Screen.Map.route,
                        enterTransition = { ScreenTransitions.slideRightIntoContainer(this) },
                        exitTransition = { ScreenTransitions.slideRightOutOfContainer(this) },
                        popEnterTransition = { ScreenTransitions.slideRightIntoContainer(this) },
                        popExitTransition = { ScreenTransitions.slideRightOutOfContainer(this) }
                    ) {
                        val viewModel = hiltViewModel<MapViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        MapScreen(
                            state = state,
                            handleEvent = viewModel.state::handleEvent
                        ) { route ->
                            navController.navigate(route) {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                        }
                    }
                }
            }
        }
    }
}