package com.example.weather.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
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
                        route = Screen.Weather.route,
                        arguments = listOf(
                            navArgument("name") { type = NavType.StringType },
                            navArgument("lat") { type = NavType.StringType },
                            navArgument("long") { type = NavType.StringType }
                        ),
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                                animationSpec = tween(700)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                animationSpec = tween(700)
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                                animationSpec = tween(700)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                animationSpec = tween(700)
                            )
                        }
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
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                animationSpec = tween(700)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                                animationSpec = tween(700)
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                animationSpec = tween(700)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                                animationSpec = tween(700)
                            )
                        }
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
                }
            }
        }
    }
}