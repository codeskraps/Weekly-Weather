package com.codeskraps.weather.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codeskraps.feature.common.navigation.Screen
import com.codeskraps.feature.weather.presentation.WeatherViewModel
import com.codeskraps.feature.weather.presentation.components.WeatherScreen
import com.codeskraps.maps.presentation.MapViewModel
import com.codeskraps.maps.presentation.components.MapScreen
import com.codeskraps.maps.presentation.RadarViewModel
import com.codeskraps.feature.settings.presentation.SettingsViewModel
import com.codeskraps.feature.settings.presentation.components.SettingsScreen
import com.codeskraps.weather.ui.theme.ScreenTransitions
import org.koin.androidx.compose.koinViewModel

@Composable
fun WeatherNavHost() {
    val navController = rememberNavController()

    val navigateToMap: () -> Unit = {
        navController.navigate(Screen.Map.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val navigateToWeather: () -> Unit = {
        navController.navigate(Screen.Weather.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val navigateToSettings: () -> Unit = {
        navController.navigate(Screen.Settings.route) {
            launchSingleTop = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Weather.route
    ) {
        composable(
            route = Screen.Weather.route,
            enterTransition = { ScreenTransitions.slideRightIntoContainer(this) },
            exitTransition = { ScreenTransitions.slideLeftOutOfContainer(this) },
            popEnterTransition = { ScreenTransitions.slideRightIntoContainer(this) },
            popExitTransition = { ScreenTransitions.slideLeftOutOfContainer(this) }
        ) {
            val viewModel = koinViewModel<WeatherViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            WeatherScreen(
                state = state,
                handleEvent = viewModel.state::handleEvent,
                action = viewModel.action,
                navigateToMap = navigateToMap,
            )
        }
        composable(
            route = Screen.Map.route,
            enterTransition = { ScreenTransitions.slideLeftIntoContainer(this) },
            exitTransition = {
                if (targetState.destination.route == Screen.Settings.route) ExitTransition.None
                else ScreenTransitions.slideRightOutOfContainer(this)
            },
            popEnterTransition = {
                if (initialState.destination.route == Screen.Settings.route) EnterTransition.None
                else ScreenTransitions.slideLeftIntoContainer(this)
            },
            popExitTransition = { ScreenTransitions.slideRightOutOfContainer(this) }
        ) {
            val mapViewModel = koinViewModel<MapViewModel>()
            val mapState by mapViewModel.state.collectAsStateWithLifecycle()

            val radarViewModel = koinViewModel<RadarViewModel>()
            val radarState by radarViewModel.state.collectAsStateWithLifecycle()

            MapScreen(
                mapState = mapState,
                handleMapEvent = mapViewModel.state::handleEvent,
                mapAction = mapViewModel.action,
                radarState = radarState,
                handleRadarEvent = radarViewModel.state::handleEvent,
                radarAction = radarViewModel.action,
                navigateToSettings = navigateToSettings,
                navigateToWeather = navigateToWeather,
            )
        }
        composable(
            route = Screen.Settings.route,
            enterTransition = { ScreenTransitions.slideLeftIntoContainer(this) },
            exitTransition = { ScreenTransitions.slideRightOutOfContainer(this) },
            popEnterTransition = { ScreenTransitions.slideLeftIntoContainer(this) },
            popExitTransition = { ScreenTransitions.slideRightOutOfContainer(this) }
        ) {
            val viewModel = koinViewModel<SettingsViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            SettingsScreen(
                state = state,
                handleEvent = viewModel.state::handleEvent,
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
