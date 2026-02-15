package com.codeskraps.feature.weather.presentation.components

import android.Manifest
import android.widget.Toast
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.filled.Map
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.codeskraps.feature.common.R
import com.codeskraps.feature.common.components.ObserveAsEvents
import com.codeskraps.feature.weather.data.mappers.toWeatherLocation
import com.codeskraps.feature.weather.presentation.mvi.WeatherAction
import com.codeskraps.feature.weather.presentation.mvi.WeatherEvent
import com.codeskraps.feature.weather.presentation.mvi.WeatherState
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeatherScreen(
    state: WeatherState,
    handleEvent: (WeatherEvent) -> Unit,
    action: Flow<WeatherAction>,
    navigateToMap: () -> Unit = {},
) {
    var showDialog by remember {
        mutableStateOf(false)
    }

    var permissionsRequested by rememberSaveable { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        if (map.values.filter { it }.size > 1) {
            handleEvent(WeatherEvent.Resume)
        }
    }

    LifecycleResumeEffect(Unit) {
        handleEvent(WeatherEvent.Resume)
        onPauseOrDispose {}
    }

    val context = LocalContext.current
    ObserveAsEvents(flow = action) { onAction ->
        when (onAction) {
            is WeatherAction.Toast -> {
                Toast.makeText(context, onAction.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    val isLoading = state.isLoading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = { handleEvent(WeatherEvent.Refresh) }
    )

    if (!permissionsRequested) {
        LaunchedEffect(Unit) {
            permissionsRequested = true
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    val resources = LocalContext.current.resources

    Scaffold(
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = navigateToMap,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = resources.getString(R.string.map_location)
                )
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (state.isLoading) {
                Text(text = "")
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.error != null || state.weatherInfo == null) {
                Text(
                    text = state.error ?: resources.getString(R.string.no_location_set),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pullRefresh(pullRefreshState)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        state.weatherInfo.currentWeatherData?.let {
                            LazyColumn {
                                item {
                                    WeatherCard(
                                        data = it,
                                        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                                        locationName = state.weatherInfo.geoLocation,
                                        temperatureUnit = state.weatherInfo.temperatureUnit,
                                        windSpeedUnit = state.weatherInfo.windSpeedUnit,
                                        topEndAction = {
                                            IconButton(
                                                onClick = {
                                                    if (state.cached && state.weatherInfo != null) {
                                                        handleEvent(WeatherEvent.Delete(state.weatherInfo.toWeatherLocation()))
                                                    } else {
                                                        showDialog = true
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = if (state.cached) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                                    tint = if (state.cached) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                                    contentDescription = resources.getString(R.string.cached)
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                        ) {
                            item {
                                state.weatherInfo.weatherDataPerDay.forEach { perDay ->
                                    Spacer(modifier = Modifier.height(16.dp))
                                    WeatherForecast(
                                        state.weatherInfo,
                                        perDay.value,
                                        handleEvent
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }

                    PullRefreshIndicator(
                        refreshing = isLoading,
                        state = pullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
            }

            state.weatherInfo?.let {
                SaveLocationDialog(
                    weatherInfo = state.weatherInfo,
                    showDialog = showDialog,
                    handleEvent = handleEvent
                ) {
                    showDialog = false
                }
            }
        }
    }
}