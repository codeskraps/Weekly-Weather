package com.trifork.feature.weather.presentation.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.NavController
import com.trifork.feature.common.components.ObserveAsEvents
import com.trifork.feature.common.navigation.Screen
import com.trifork.feature.common.R
import com.trifork.feature.weather.data.mappers.toWeatherLocation
import com.trifork.feature.weather.domain.model.WeatherLocation
import com.trifork.feature.weather.presentation.mvi.WeatherAction
import com.trifork.feature.weather.presentation.mvi.WeatherEvent
import com.trifork.feature.weather.presentation.mvi.WeatherState
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun WeatherScreen(
    navController: NavController,
    state: WeatherState,
    handleEvent: (WeatherEvent) -> Unit,
    action: Flow<WeatherAction>
) {

    var menuExpanded by remember {
        mutableStateOf(false)
    }
    var showDialog by remember {
        mutableStateOf(false)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        if (map.values.filter { it }.size > 1) {
            handleEvent(WeatherEvent.Refresh)
        }
    }

    LifecycleResumeEffect(Unit) {
        handleEvent(WeatherEvent.Resume)
        onPauseOrDispose {}
    }

    val context = LocalContext.current
    ObserveAsEvents(flow = action) { onAction ->
        when (onAction) {
            is WeatherAction.Toast -> Toast.makeText(context, onAction.message, Toast.LENGTH_LONG)
                .show()
        }
    }

    val isLoading = state.isLoading
    val pullRefreshState = rememberPullRefreshState(isLoading, {
        handleEvent(WeatherEvent.Refresh)
    })


    LaunchedEffect(key1 = Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    val resources = LocalContext.current.resources

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
                            resources.getString(R.string.error)
                        } else if (state.isLoading) {
                            resources.getString(R.string.loading)
                        } else {
                            state.weatherInfo?.geoLocation ?: ""
                        }
                    )
                },
                actions = {
                    IconButton(onClick = {
                        if (state.cached && state.weatherInfo != null) {
                            handleEvent(WeatherEvent.Delete(state.weatherInfo.toWeatherLocation()))
                        } else {
                            showDialog = true
                        }
                    }) {
                        Icon(
                            imageVector = if (state.cached) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            tint = if (state.cached) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.outline,
                            contentDescription = resources.getString(R.string.cached)
                        )
                    }
                    IconButton(onClick = {
                        handleEvent(
                            WeatherEvent.LoadWeatherInfo(WeatherLocation())
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = resources.getString(R.string.current_location)
                        )
                    }
                    /*
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "Menu"
                        )
                    }
                    DropdownMenu(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background),
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false })
                    {
                        DropdownMenuItem(
                            onClick = { showDialog = true },
                            text = { Text(text = "Save Location") }
                        )
                        DropdownMenuItem(
                            onClick = { /*TODO*/ },
                            text = { Text(text = "About") }
                        )
                    }*/
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
                        contentDescription = resources.getString(R.string.search)
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
            } else if (state.error != null || state.weatherInfo == null) {
                Text(
                    text = state.error ?: resources.getString(R.string.no_location_set),
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
                                    data = state.weatherInfo.currentWeatherData!!,
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