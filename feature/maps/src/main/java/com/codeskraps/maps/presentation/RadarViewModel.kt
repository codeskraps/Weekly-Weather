package com.codeskraps.maps.presentation

import androidx.lifecycle.viewModelScope
import com.codeskraps.core.local.domain.model.RadarSpeed
import com.codeskraps.core.local.domain.repository.LocalResourceRepository
import com.codeskraps.core.local.domain.repository.SettingsRepository
import com.codeskraps.core.location.domain.LocationTracker
import com.codeskraps.feature.common.dispatcher.DispatcherProvider
import com.codeskraps.feature.common.mvi.StateReducerViewModel
import com.codeskraps.feature.common.util.Resource
import com.codeskraps.maps.data.remote.RadarTileProvider
import com.codeskraps.maps.domain.model.RadarFrame
import com.codeskraps.maps.domain.repository.RadarRepository
import com.codeskraps.maps.presentation.mvi.RadarAction
import com.codeskraps.maps.presentation.mvi.RadarEvent
import com.codeskraps.maps.presentation.mvi.RadarState
import com.codeskraps.umami.domain.AnalyticsRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RadarViewModel(
    private val radarRepository: RadarRepository,
    private val locationTracker: LocationTracker,
    private val localResource: LocalResourceRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val analyticsRepository: AnalyticsRepository,
    private val settingsRepository: SettingsRepository,
) : StateReducerViewModel<RadarState, RadarEvent, RadarAction>(RadarState.initial) {

    private var animationJob: Job? = null
    private var lastFetchTime: Long = 0
    private companion object {
        const val RADAR_CACHE_DURATION = 5 * 60 * 1000L // 5 minutes
    }

    override fun reduceState(currentState: RadarState, event: RadarEvent): RadarState {
        return when (event) {
            is RadarEvent.Resume -> onResume(currentState)
            is RadarEvent.Pause -> onPause(currentState)
            is RadarEvent.RadarDataLoaded -> onRadarDataLoaded(currentState, event.frames, event.isDoubleSpeed)
            is RadarEvent.Error -> onError(currentState, event.message)
            is RadarEvent.PlayPause -> onPlayPause(currentState)
            is RadarEvent.SeekToFrame -> onSeekToFrame(currentState, event.index)
            is RadarEvent.NextFrame -> onNextFrame(currentState)
            is RadarEvent.LocationUpdated -> onLocationUpdated(currentState, event.location)
            is RadarEvent.ToggleSpeed -> onToggleSpeed(currentState)
        }
    }

    private fun onPause(currentState: RadarState): RadarState {
        stopAnimation()
        return currentState.copy(isPlaying = false)
    }

    private fun onResume(currentState: RadarState): RadarState {
        val now = System.currentTimeMillis()
        if (currentState.radarFrames.isNotEmpty() && now - lastFetchTime < RADAR_CACHE_DURATION) {
            startAnimation(currentState.isDoubleSpeed)
            return currentState.copy(isPlaying = true)
        }

        viewModelScope.launch(dispatcherProvider.io) {
            analyticsRepository.trackPageView("radar")

            val settings = settingsRepository.settings.first()
            val isDoubleSpeed = settings.radarSpeed == RadarSpeed.FAST

            locationTracker.getCurrentLocation()?.let {
                state.handleEvent(RadarEvent.LocationUpdated(LatLng(it.latitude, it.longitude)))
            }

            when (val result = radarRepository.getRadarFrames()) {
                is Resource.Success -> {
                    state.handleEvent(RadarEvent.RadarDataLoaded(result.data.allFrames, isDoubleSpeed))
                }
                is Resource.Error -> {
                    state.handleEvent(RadarEvent.Error(result.message))
                }
            }
        }
        return currentState.copy(isLoading = true, error = null)
    }

    private fun onRadarDataLoaded(currentState: RadarState, frames: List<RadarFrame>, isDoubleSpeed: Boolean): RadarState {
        lastFetchTime = System.currentTimeMillis()
        startAnimation(isDoubleSpeed)
        return currentState.copy(
            isLoading = false,
            radarFrames = frames,
            currentFrameIndex = 0,
            isPlaying = true,
            isDoubleSpeed = isDoubleSpeed,
            error = null
        )
    }

    private fun onError(currentState: RadarState, message: String): RadarState {
        viewModelScope.launch {
            actionChannel.send(RadarAction.ShowToast(message))
        }
        return currentState.copy(isLoading = false, error = message)
    }

    private fun onPlayPause(currentState: RadarState): RadarState {
        if (currentState.isPlaying) {
            stopAnimation()
        } else {
            startAnimation(currentState.isDoubleSpeed)
        }
        return currentState.copy(isPlaying = !currentState.isPlaying)
    }

    private fun onSeekToFrame(currentState: RadarState, index: Int): RadarState {
        stopAnimation()
        return currentState.copy(
            currentFrameIndex = index.coerceIn(0, (currentState.radarFrames.size - 1).coerceAtLeast(0)),
            isPlaying = false
        )
    }

    private fun onNextFrame(currentState: RadarState): RadarState {
        if (currentState.radarFrames.isEmpty()) return currentState
        val nextIndex = (currentState.currentFrameIndex + 1) % currentState.radarFrames.size
        return currentState.copy(currentFrameIndex = nextIndex)
    }

    private fun onLocationUpdated(currentState: RadarState, location: LatLng): RadarState {
        return currentState.copy(userLocation = location)
    }

    private fun onToggleSpeed(currentState: RadarState): RadarState {
        val newState = currentState.copy(isDoubleSpeed = !currentState.isDoubleSpeed)
        if (newState.isPlaying) {
            startAnimation(newState.isDoubleSpeed)
        }
        return newState
    }

    private fun startAnimation(doubleSpeed: Boolean = false) {
        animationJob?.cancel()
        val delayMs = if (doubleSpeed) 500L else 1000L
        animationJob = viewModelScope.launch {
            while (true) {
                delay(delayMs)
                state.handleEvent(RadarEvent.NextFrame)
            }
        }
    }

    private fun stopAnimation() {
        animationJob?.cancel()
        animationJob = null
    }

    override fun onCleared() {
        super.onCleared()
        animationJob?.cancel()
        RadarTileProvider.clearCache()
    }
}
