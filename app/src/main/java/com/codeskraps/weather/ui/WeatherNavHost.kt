package com.codeskraps.weather.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.codeskraps.feature.common.navigation.Screen
import com.codeskraps.feature.geocoding.presentation.GeocodingViewModel
import com.codeskraps.feature.geocoding.presentation.components.GeocodingScreen
import com.codeskraps.feature.weather.presentation.WeatherViewModel
import com.codeskraps.feature.weather.presentation.components.WeatherScreen
import com.codeskraps.maps.presentation.MapViewModel
import com.codeskraps.maps.presentation.components.MapScreen
import com.codeskraps.feature.radar.presentation.RadarViewModel
import com.codeskraps.feature.radar.presentation.components.RadarScreen
import com.codeskraps.feature.settings.presentation.SettingsViewModel
import com.codeskraps.feature.settings.presentation.components.SettingsScreen
import com.codeskraps.weather.R
import com.codeskraps.weather.ui.theme.ScreenTransitions
import org.koin.androidx.compose.koinViewModel

enum class TopDestination(
    val route: String,
    val icon: ImageVector,
    val labelRes: Int,
) {
    WEATHER(
        route = Screen.Weather.route,
        icon = Icons.Default.Home,
        labelRes = R.string.nav_weather,
    ),
    SEARCH(
        route = Screen.Geocoding.route,
        icon = Icons.Default.Search,
        labelRes = R.string.nav_search,
    ),
    MAP(
        route = Screen.Map.route,
        icon = Icons.Default.Place,
        labelRes = R.string.nav_map,
    ),
    RADAR(
        route = Screen.Radar.route,
        icon = Icons.Default.Refresh,
        labelRes = R.string.nav_radar,
    ),
    SETTINGS(
        route = Screen.Settings.route,
        icon = Icons.Default.Settings,
        labelRes = R.string.nav_settings,
    ),
}

@Composable
fun WeatherNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val layoutType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())
    val isBottomBar = layoutType == NavigationSuiteType.NavigationBar

    val navigationSuiteColors = NavigationSuiteDefaults.colors(
        navigationBarContainerColor = MaterialTheme.colorScheme.primaryContainer,
        navigationRailContainerColor = MaterialTheme.colorScheme.primaryContainer,
        navigationDrawerContainerColor = MaterialTheme.colorScheme.primaryContainer,
    )

    val itemColors = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unselectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unselectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    )

    NavigationSuiteScaffold(
        navigationSuiteColors = navigationSuiteColors,
        navigationSuiteItems = {
            TopDestination.entries.forEach { dest ->
                item(
                    icon = {
                        Icon(
                            imageVector = dest.icon,
                            contentDescription = stringResource(dest.labelRes)
                        )
                    },
                    label = if (isBottomBar) null else {{ Text(stringResource(dest.labelRes)) }},
                    selected = currentRoute == dest.route,
                    colors = itemColors,
                    onClick = {
                        val targetRoute = when (dest) {
                            TopDestination.WEATHER -> Screen.Weather.createRoute(
                                "Current Location", 0.0, 0.0
                            )
                            TopDestination.SEARCH -> Screen.Geocoding.route
                            TopDestination.MAP -> Screen.Map.route
                            TopDestination.RADAR -> Screen.Radar.route
                            TopDestination.SETTINGS -> Screen.Settings.route
                        }
                        navController.navigate(targetRoute) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Weather.createRoute(
                name = "Current Location",
                lat = 0.0,
                long = 0.0
            )
        ) {
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
                val viewModel = koinViewModel<WeatherViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                WeatherScreen(
                    state = state,
                    handleEvent = viewModel.state::handleEvent,
                    action = viewModel.action
                )
            }
            composable(
                route = Screen.Geocoding.route,
                enterTransition = { ScreenTransitions.slideLeftIntoContainer(this) },
                exitTransition = { ScreenTransitions.slideRightOutOfContainer(this) },
                popEnterTransition = { ScreenTransitions.slideLeftIntoContainer(this) },
                popExitTransition = { ScreenTransitions.slideRightOutOfContainer(this) }
            ) {
                val viewModel = koinViewModel<GeocodingViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                GeocodingScreen(
                    state = state,
                    handleEvent = viewModel.state::handleEvent,
                    action = viewModel.action,
                ) { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = false
                        }
                    }
                }
            }
            composable(
                route = Screen.Map.route,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(700)
                    )
                }
            ) {
                val viewModel = koinViewModel<MapViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()
                MapScreen(
                    state = state,
                    handleEvent = viewModel.state::handleEvent,
                    action = viewModel.action,
                    navRoute = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = false
                            }
                        }
                    }
                )
            }
            composable(
                route = Screen.Radar.route,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(700)
                    )
                }
            ) {
                val viewModel = koinViewModel<RadarViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()
                RadarScreen(
                    state = state,
                    handleEvent = viewModel.state::handleEvent,
                    action = viewModel.action,
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
                )
            }
        }
    }
}
