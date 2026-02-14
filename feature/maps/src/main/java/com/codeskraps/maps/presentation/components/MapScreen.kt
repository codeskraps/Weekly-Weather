package com.codeskraps.maps.presentation.components

import android.content.res.Resources
import android.util.Log
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.codeskraps.feature.common.R
import com.codeskraps.feature.common.components.ObserveAsEvents
import com.codeskraps.feature.common.navigation.Screen
import com.codeskraps.maps.presentation.mvi.MapAction
import com.codeskraps.maps.presentation.mvi.MapEvent
import com.codeskraps.maps.presentation.mvi.MapState
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    state: MapState,
    handleEvent: (MapEvent) -> Unit,
    action: Flow<MapAction>,
    navRoute: (String) -> Unit
) {
    val context = LocalContext.current
    val resources = context.resources
    val tag = "WeatherApp:MapScreen"

    ObserveAsEvents(action) { mapAction ->
        when (mapAction) {
            is MapAction.ShowToast -> {
                Log.i(tag, "Showing toast: ${mapAction.message}")
                Toast.makeText(context, mapAction.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 10f)
    }

    // Use LaunchedEffect to update camera position when location changes
    LaunchedEffect(state.location) {
        state.location?.let {
            Log.i(tag, "Updating camera position to: $it")
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 10f)
        }
    }

    LifecycleResumeEffect(Unit) {
        Log.i(tag, "Screen resumed, sending Resume event")
        handleEvent(MapEvent.Resume)
        onPauseOrDispose {
            Log.i(tag, "Screen paused/disposed")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(resources.getString(R.string.map_location))
                },
                actions = {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(15.dp)
                        )
                    } else {
                        IconButton(
                            onClick = {
                                val route = Screen.Weather.createRoute(
                                    resources.getString(R.string.map_location),
                                    cameraPositionState.position.target.latitude,
                                    cameraPositionState.position.target.longitude
                                )
                                Log.i(tag, "Add location clicked, navigating to route: $route")
                                navRoute(route)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = resources.getString(R.string.add)
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                cameraPositionState = cameraPositionState
            ) {
                state.geoLocations.forEach { geoLocation ->
                    Marker(
                        state = MarkerState(
                            position = LatLng(
                                geoLocation.latitude,
                                geoLocation.longitude
                            )
                        ),
                        title = geoLocation.name,
                        icon = vectorToBitmap(
                            resources = resources,
                            id = R.drawable.ic_location,
                            color = MaterialTheme.colorScheme.surface.toArgb()
                        )
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.Place,
                tint = MaterialTheme.colorScheme.surface,
                contentDescription = resources.getString(R.string.map_location),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

private fun vectorToBitmap(
    resources: Resources,
    @DrawableRes id: Int,
    @ColorInt color: Int
): BitmapDescriptor {
    val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
    val bitmap = createBitmap(vectorDrawable!!.intrinsicWidth, vectorDrawable.intrinsicHeight)
    val canvas = android.graphics.Canvas(bitmap)
    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
    DrawableCompat.setTint(vectorDrawable, color)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}