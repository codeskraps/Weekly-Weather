package com.example.weather.feature.weather.presentation.components

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weather.app.navigation.Screen
import com.example.weather.feature.weather.domain.model.WeatherLocation
import com.example.weather.feature.weather.presentation.WeatherViewModel
import com.example.weather.feature.weather.presentation.mvi.WeatherEvent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun WeatherScreen(
    navController: NavController,
    viewModel: WeatherViewModel,
    geoLocation: WeatherLocation
) {
    val state by viewModel.state.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        if (map.values.filter { it }.size > 1) {
            viewModel.state.handleEvent(WeatherEvent.LoadWeatherInfo(geoLocation))
        }
    }

    val isLoading = state.isLoading
    val pullRefreshState = rememberPullRefreshState(isLoading, {
        viewModel.state.handleEvent(
            WeatherEvent.LoadWeatherInfo(
                geoLocation
            )
        )
    })


    LaunchedEffect(key1 = Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        if (state.error != null) {
                            "Error"
                        } else if (state.isLoading) {
                            "Loading ..."
                        } else {
                            state.weatherInfo?.geoLocation ?: ""
                        }
                    )
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.state.handleEvent(
                            WeatherEvent.LoadWeatherInfo(
                                geoLocation
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "Refresh"
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            if (state.weatherInfo != null) {
                FloatingActionButton(
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    onClick = { navController.navigate(Screen.Geocoding.route) }) {
                    Icon(
                        Icons.Default.Search,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "Search"
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.error != null) {
                Text(
                    text = state.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(Modifier.pullRefresh(pullRefreshState)) {

                        LazyColumn {
                            item {
                                WeatherCard(
                                    data = state.weatherInfo!!.currentWeatherData!!,
                                    backgroundColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            }
                        }

                        PullRefreshIndicator(
                            refreshing = false,
                            state = pullRefreshState,
                            Modifier.align(Alignment.TopCenter)
                        )
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        item {
                            state.weatherInfo!!.weatherDataPerDay.forEach { perDay ->
                                Spacer(modifier = Modifier.height(16.dp))
                                WeatherForecast(
                                    state.weatherInfo!!,
                                    perDay.value,
                                    viewModel
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}