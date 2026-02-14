package com.codeskraps.feature.radar.presentation.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.codeskraps.feature.common.components.ObserveAsEvents
import com.codeskraps.feature.radar.R
import com.codeskraps.feature.radar.presentation.mvi.RadarAction
import com.codeskraps.feature.radar.presentation.mvi.RadarEvent
import com.codeskraps.feature.radar.presentation.mvi.RadarState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.TileOverlay
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RadarScreen(
    state: RadarState,
    handleEvent: (RadarEvent) -> Unit,
    action: Flow<RadarAction>,
) {
    val context = LocalContext.current

    ObserveAsEvents(action) { radarAction ->
        when (radarAction) {
            is RadarAction.ShowToast -> {
                Toast.makeText(context, radarAction.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LifecycleResumeEffect(Unit) {
        handleEvent(RadarEvent.Resume)
        onPauseOrDispose { }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 5f)
    }

    LaunchedEffect(state.userLocation) {
        state.userLocation?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 5f)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("Radar") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
            ) {
                state.currentFrame?.let { frame ->
                    key(frame.tileUrl) {
                        TileOverlay(
                            tileProvider = RadarTileProvider(frame.tileUrl),
                            transparency = 0.3f
                        )
                    }
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            if (state.radarFrames.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        state.currentFrame?.let { frame ->
                            val formattedTime = remember(frame.timestamp) {
                                val instant = Instant.ofEpochSecond(frame.timestamp)
                                val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                                dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                            }
                            Text(
                                text = formattedTime,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 16.sp
                            )
                        }

                        Slider(
                            value = state.currentFrameIndex.toFloat(),
                            onValueChange = { handleEvent(RadarEvent.SeekToFrame(it.toInt())) },
                            valueRange = 0f..(state.radarFrames.size - 1).toFloat().coerceAtLeast(0f),
                            steps = (state.radarFrames.size - 2).coerceAtLeast(0),
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {
                                val prevIndex = if (state.currentFrameIndex > 0)
                                    state.currentFrameIndex - 1
                                else
                                    state.radarFrames.size - 1
                                handleEvent(RadarEvent.SeekToFrame(prevIndex))
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Previous",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            IconButton(onClick = { handleEvent(RadarEvent.PlayPause) }) {
                                Icon(
                                    imageVector = if (state.isPlaying)
                                        ImageVector.vectorResource(id = R.drawable.ic_pause)
                                    else
                                        Icons.Default.PlayArrow,
                                    contentDescription = if (state.isPlaying) "Pause" else "Play",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            IconButton(onClick = {
                                val nextIndex = (state.currentFrameIndex + 1) % state.radarFrames.size
                                handleEvent(RadarEvent.SeekToFrame(nextIndex))
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Next",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            state.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
