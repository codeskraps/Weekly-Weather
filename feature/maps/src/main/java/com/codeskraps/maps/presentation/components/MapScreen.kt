package com.codeskraps.maps.presentation.components

import android.content.res.Resources
import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.codeskraps.feature.common.R
import com.codeskraps.feature.common.navigation.Screen
import com.codeskraps.maps.presentation.mvi.MapEvent
import com.codeskraps.maps.presentation.mvi.MapState
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    state: MapState,
    handleEvent: (MapEvent) -> Unit,
    navRoute: (String) -> Unit
) {
    val context = LocalContext.current
    val resources = context.resources

    var cameraPositionState: CameraPositionState? = null
    if (state.location != null) {
        val location = state.location
        cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(location, 10f)
        }
    }

    LifecycleResumeEffect(Unit) {
        handleEvent(MapEvent.Resume)
        onPauseOrDispose {}
    }

    BackHandler {
        navRoute(Screen.Weather.route)
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
                navigationIcon = {
                    IconButton(onClick = { navRoute(Screen.Weather.route) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = resources.getString(R.string.back)
                        )
                    }
                },
                actions = {
                    if (cameraPositionState == null) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(15.dp)
                        )
                    } else {
                        IconButton(onClick = {
                            navRoute(
                                Screen.Weather.createRoute(
                                    resources.getString(R.string.map_location),
                                    cameraPositionState.position.target.latitude,
                                    cameraPositionState.position.target.longitude
                                )
                            )
                        }) {
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

        if (cameraPositionState != null) {
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
}

private fun vectorToBitmap(
    resources: Resources,
    @DrawableRes id: Int,
    @ColorInt color: Int
): BitmapDescriptor {
    val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
    val bitmap = Bitmap.createBitmap(
        vectorDrawable!!.intrinsicWidth,
        vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = android.graphics.Canvas(bitmap)
    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
    DrawableCompat.setTint(vectorDrawable, color)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}