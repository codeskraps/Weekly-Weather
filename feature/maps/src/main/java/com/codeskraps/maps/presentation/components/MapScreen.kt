package com.codeskraps.maps.presentation.components

import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.codeskraps.feature.common.R
import com.codeskraps.feature.common.components.ObserveAsEvents
import com.codeskraps.feature.common.components.OverflowMenu
import com.codeskraps.maps.R as MapsR
import com.codeskraps.maps.data.remote.RadarTileProvider
import com.codeskraps.maps.presentation.mvi.MapAction
import com.codeskraps.maps.presentation.mvi.MapEvent
import com.codeskraps.maps.presentation.mvi.MapState
import com.codeskraps.maps.presentation.mvi.RadarAction
import com.codeskraps.maps.presentation.mvi.RadarEvent
import com.codeskraps.maps.presentation.mvi.RadarState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.TileOverlay
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun MapScreen(
    mapState: MapState,
    handleMapEvent: (MapEvent) -> Unit,
    mapAction: Flow<MapAction>,
    radarState: RadarState,
    handleRadarEvent: (RadarEvent) -> Unit,
    radarAction: Flow<RadarAction>,
    navigateToSettings: () -> Unit = {},
    navigateToWeather: () -> Unit = {},
) {
    val context = LocalContext.current
    val resources = context.resources
    val tag = "WeatherApp:MapScreen"
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    ObserveAsEvents(mapAction) { action ->
        when (action) {
            is MapAction.ShowToast -> {
                Log.i(tag, "Showing toast: ${action.message}")
                Toast.makeText(context, action.message, Toast.LENGTH_SHORT).show()
            }
            is MapAction.ClearSearchFocus -> {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        }
    }

    ObserveAsEvents(radarAction) { action ->
        when (action) {
            is RadarAction.ShowToast -> {
                Toast.makeText(context, action.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), mapState.zoom)
    }

    LaunchedEffect(mapState.location) {
        mapState.location?.let {
            Log.i(tag, "Updating camera position to: $it, zoom: ${mapState.zoom}")
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, mapState.zoom)
        }
    }

    // Detect user pan and clear location name
    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving && mapState.locationName.isNotEmpty()) {
            val target = cameraPositionState.position.target
            mapState.location?.let { loc ->
                if (kotlin.math.abs(loc.latitude - target.latitude) > 1e-4 ||
                    kotlin.math.abs(loc.longitude - target.longitude) > 1e-4) {
                    handleMapEvent(MapEvent.CameraMoved)
                }
            }
        }
    }

    LifecycleResumeEffect(Unit) {
        Log.i(tag, "Screen resumed, sending Resume events")
        handleMapEvent(MapEvent.Resume)
        if (mapState.isRadarMode) {
            handleRadarEvent(RadarEvent.Resume)
        }
        onPauseOrDispose {
            val target = cameraPositionState.position.target
            if (target.latitude != 0.0 || target.longitude != 0.0) {
                Log.i(tag, "Screen paused, syncing camera position: $target")
                handleMapEvent(MapEvent.CameraIdle(target, cameraPositionState.position.zoom))
            }
            handleRadarEvent(RadarEvent.Pause)
        }
    }

    // Zoom out to max radar zoom level when entering radar mode
    LaunchedEffect(mapState.isRadarMode) {
        if (mapState.isRadarMode && cameraPositionState.position.zoom > 7f) {
            cameraPositionState.animate(
                CameraUpdateFactory.zoomTo(7f)
            )
        }
    }

    val statusBarPadding = WindowInsets.statusBars.asPaddingValues()
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()
    val scope = rememberCoroutineScope()
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Stagger radar overlay creation to avoid burst of tile requests
    var radarOverlayCount by remember { mutableIntStateOf(0) }
    LaunchedEffect(radarState.radarFrames) {
        if (radarState.radarFrames.isNotEmpty()) {
            radarOverlayCount = 1
            for (i in 2..radarState.radarFrames.size) {
                delay(200)
                radarOverlayCount = i
            }
        } else {
            radarOverlayCount = 0
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Layer 1: Google Map
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                zoomGesturesEnabled = true
            ),
            properties = if (mapState.isRadarMode) {
                MapProperties(maxZoomPreference = 7f)
            } else {
                MapProperties()
            }
        ) {
            // Radar tile overlay (only in radar mode)
            // Overlays are created gradually to avoid a burst of tile requests
            if (mapState.isRadarMode) {
                radarState.radarFrames.forEachIndexed { index, frame ->
                    if (index == radarState.currentFrameIndex || index < radarOverlayCount) {
                        key(frame.tileUrl) {
                            TileOverlay(
                                tileProvider = remember(frame.tileUrl) { RadarTileProvider(frame.tileUrl) },
                                transparency = 0.3f,
                                visible = index == radarState.currentFrameIndex,
                                fadeIn = false
                            )
                        }
                    }
                }
            }
        }

        // Layer 2: Center place icon (only in search mode)
        if (!mapState.isRadarMode) {
            Icon(
                imageVector = Icons.Default.Place,
                tint = MaterialTheme.colorScheme.surface,
                contentDescription = resources.getString(R.string.map_location),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Layer 3: Radar loading indicator (only in radar mode)
        if (mapState.isRadarMode && radarState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Radar error (only in radar mode)
        if (mapState.isRadarMode) {
            radarState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        // Layer 4: Radar panel at TOP (slides down when entering radar mode)
        AnimatedVisibility(
            visible = mapState.isRadarMode && radarState.radarFrames.isNotEmpty(),
            enter = slideInVertically(initialOffsetY = { -it }),
            exit = slideOutVertically(targetOffsetY = { -it }),
            modifier = Modifier.align(if (isLandscape) Alignment.TopStart else Alignment.TopCenter)
        ) {
            Card(
                modifier = Modifier
                    .then(if (isLandscape) Modifier.widthIn(max = 480.dp) else Modifier.fillMaxWidth())
                    .padding(
                        top = statusBarPadding.calculateTopPadding() + 8.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    radarState.currentFrame?.let { frame ->
                        val formattedTime = remember(frame.timestamp) {
                            val instant = Instant.ofEpochSecond(frame.timestamp)
                            val dateTime =
                                LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                            dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                        }
                        Text(
                            text = formattedTime,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp
                        )
                    }

                    Slider(
                        value = radarState.currentFrameIndex.toFloat(),
                        onValueChange = {
                            handleRadarEvent(RadarEvent.SeekToFrame(it.toInt()))
                        },
                        valueRange = 0f..(radarState.radarFrames.size - 1).toFloat()
                            .coerceAtLeast(0f),
                        steps = (radarState.radarFrames.size - 2).coerceAtLeast(0),
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(onClick = {
                            val prevIndex = if (radarState.currentFrameIndex > 0)
                                radarState.currentFrameIndex - 1
                            else
                                radarState.radarFrames.size - 1
                            handleRadarEvent(RadarEvent.SeekToFrame(prevIndex))
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = resources.getString(R.string.previous),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        IconButton(onClick = { handleRadarEvent(RadarEvent.PlayPause) }) {
                            Icon(
                                imageVector = if (radarState.isPlaying)
                                    ImageVector.vectorResource(id = MapsR.drawable.ic_pause)
                                else
                                    Icons.Default.PlayArrow,
                                contentDescription = if (radarState.isPlaying) resources.getString(R.string.pause) else resources.getString(R.string.play),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        IconButton(onClick = {
                            val nextIndex =
                                (radarState.currentFrameIndex + 1) % radarState.radarFrames.size
                            handleRadarEvent(RadarEvent.SeekToFrame(nextIndex))
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = resources.getString(R.string.next),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                            Text(
                                text = if (radarState.isDoubleSpeed) "x2" else "x1",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .clickable { handleRadarEvent(RadarEvent.ToggleSpeed) }
                                    .background(
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = if (radarState.isDoubleSpeed) 0.15f else 0f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Layer 5: Search overlay (only in search mode)
        AnimatedVisibility(
            visible = !mapState.isRadarMode && mapState.isSearchFocused,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { handleMapEvent(MapEvent.DismissSearch) },
                color = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = statusBarPadding.calculateTopPadding() + 80.dp)
                ) {
                when {
                    mapState.isSearchLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    mapState.searchError != null -> {
                        Text(
                            text = mapState.searchError,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    mapState.searchResults.isEmpty() && mapState.searchQuery.isNotEmpty() -> {
                        Text(
                            text = resources.getString(R.string.no_results),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    mapState.searchResults.isEmpty() -> {
                        Text(
                            text = resources.getString(R.string.search_locations),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            itemsIndexed(mapState.searchResults) { index, geoLocation ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            handleMapEvent(MapEvent.SelectLocation(geoLocation))
                                        }
                                        .padding(horizontal = 24.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = geoLocation.displayName(),
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(end = 8.dp),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Icon(
                                        imageVector = if (geoLocation.cached) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        tint = if (geoLocation.cached) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                        contentDescription = resources.getString(R.string.cached),
                                        modifier = Modifier
                                            .clickable {
                                                handleMapEvent(
                                                    if (geoLocation.cached) {
                                                        MapEvent.DeleteLocation(geoLocation)
                                                    } else {
                                                        MapEvent.SaveLocation(geoLocation)
                                                    }
                                                )
                                            }
                                    )
                                }
                                if (index < mapState.searchResults.lastIndex) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                }
            }
        }

        // Layer 6: Search bar (slides up when entering radar mode)
        AnimatedVisibility(
            visible = !mapState.isRadarMode,
            enter = slideInVertically(initialOffsetY = { -it }),
            exit = slideOutVertically(targetOffsetY = { -it }),
            modifier = Modifier.align(if (isLandscape) Alignment.TopStart else Alignment.TopCenter)
        ) {
            val focusRequester = remember { FocusRequester() }

            Card(
                modifier = Modifier
                    .then(if (isLandscape) Modifier.widthIn(max = 480.dp) else Modifier.fillMaxWidth())
                    .padding(
                        top = statusBarPadding.calculateTopPadding() + 8.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (mapState.isSearchFocused) {
                        IconButton(onClick = { handleMapEvent(MapEvent.DismissSearch) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = resources.getString(R.string.back)
                            )
                        }
                    } else {
                        IconButton(onClick = { focusRequester.requestFocus() }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = resources.getString(R.string.search)
                            )
                        }
                    }

                    TextField(
                        value = mapState.searchQuery,
                        onValueChange = { handleMapEvent(MapEvent.Search(it)) },
                        placeholder = {
                            Text(
                                text = mapState.locationName.ifEmpty {
                                    resources.getString(R.string.search_locations)
                                },
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                handleMapEvent(MapEvent.SearchFocusChanged(focusState.isFocused))
                            },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                keyboardController?.hide()
                            }
                        )
                    )

                    OverflowMenu(onSettingsClick = navigateToSettings)
                }
            }
        }

        // Layer 7: Zoom controls at bottom-left
        if (!mapState.isSearchFocused) Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp + navigationBarPadding.calculateBottomPadding()
                ),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            SmallFloatingActionButton(
                onClick = { scope.launch { cameraPositionState.animate(CameraUpdateFactory.zoomIn()) } },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = resources.getString(R.string.zoom_in)
                )
            }
            SmallFloatingActionButton(
                onClick = { scope.launch { cameraPositionState.animate(CameraUpdateFactory.zoomOut()) } },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = resources.getString(R.string.zoom_out)
                )
            }
        }

        // Layer 8: FABs – small toggle on top, big navigation below
        if (!mapState.isSearchFocused) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 16.dp + navigationBarPadding.calculateBottomPadding()
                    ),
                horizontalAlignment = Alignment.End,
            ) {
                // GPS tracking toggle
                FloatingActionButton(
                    onClick = { handleMapEvent(MapEvent.ToggleGps) },
                    shape = RoundedCornerShape(18.dp),
                    containerColor = if (mapState.isGpsTracking)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.background,
                    contentColor = if (mapState.isGpsTracking)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onBackground
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = resources.getString(R.string.current_location)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Radar mode toggle
                FloatingActionButton(
                    onClick = {
                        if (mapState.isRadarMode) {
                            handleRadarEvent(RadarEvent.Pause)
                        } else {
                            handleRadarEvent(RadarEvent.Resume)
                        }
                        handleMapEvent(MapEvent.ToggleRadarMode)
                    },
                    shape = RoundedCornerShape(18.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = if (mapState.isRadarMode)
                            Icons.Default.Search
                        else
                            Icons.Default.PlayArrow,
                        contentDescription = if (mapState.isRadarMode)
                            resources.getString(R.string.map_location)
                        else
                            resources.getString(R.string.radar)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Big FAB: navigate to Weather screen
                LargeFloatingActionButton(
                    onClick = {
                        if (mapState.isRadarMode) {
                            handleRadarEvent(RadarEvent.Pause)
                            handleMapEvent(MapEvent.ToggleRadarMode)
                        }
                        navigateToWeather()
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = resources.getString(R.string.nav_weather)
                    )
                }
            }
        }
    }
}
