package com.codeskraps.weather.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.codeskraps.feature.common.navigation.Screen
import com.codeskraps.feature.geocoding.presentation.GeocodingViewModel
import com.codeskraps.feature.geocoding.presentation.components.GeocodingScreen
import com.codeskraps.feature.weather.presentation.WeatherViewModel
import com.codeskraps.feature.weather.presentation.components.WeatherScreen
import com.codeskraps.maps.presentation.MapViewModel
import com.codeskraps.maps.presentation.components.MapScreen
import com.codeskraps.umami.domain.AnalyticsRepository
import com.codeskraps.weather.ui.theme.ScreenTransitions
import com.codeskraps.weather.ui.theme.WeatherTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var analyticsRepository: AnalyticsRepository

    private var isAnalyticsInitialized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Keep the splash screen visible until analytics initialization is complete
        splashScreen.setKeepOnScreenCondition { !isAnalyticsInitialized }

        // Initialize analytics in a coroutine
        lifecycleScope.launch {
            analyticsRepository.initialize()
            isAnalyticsInitialized = true
        }

        // Set up the OnPreDrawListener to the root view
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : android.view.ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check if analytics is initialized
                    return if (isAnalyticsInitialized) {
                        // The content is ready; remove the listener and start drawing
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        // The content isn't ready; suspend drawing
                        false
                    }
                }
            }
        )

        setContent {
            WeatherTheme {
                val navController = rememberNavController()

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
                        Log.d("Navigation", "Attempting to navigate to Map screen")
                        val viewModel = hiltViewModel<MapViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()
                        MapScreen(
                            state = state,
                            handleEvent = viewModel.state::handleEvent,
                            action = viewModel.action,
                            navRoute = { route ->
                                when (route) {
                                    "nav_up" -> {
                                        Log.d("Navigation", "Map screen navigation up")
                                        navController.navigateUp()
                                    }

                                    else -> {
                                        Log.d("Navigation", "Map screen navigation to $route")
                                        navController.navigate(route)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}