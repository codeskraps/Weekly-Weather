package com.codeskraps.feature.radar.presentation

import androidx.lifecycle.viewModelScope
import com.codeskraps.core.local.domain.repository.LocalResourceRepository
import com.codeskraps.core.location.domain.LocationTracker
import com.codeskraps.feature.common.dispatcher.DispatcherProvider
import com.codeskraps.feature.common.mvi.StateReducerViewModel
import com.codeskraps.feature.common.util.Resource
import com.codeskraps.feature.radar.domain.model.RadarFrame
import com.codeskraps.feature.radar.domain.repository.RadarRepository
import com.codeskraps.feature.radar.presentation.mvi.RadarAction
import com.codeskraps.feature.radar.presentation.mvi.RadarEvent
import com.codeskraps.feature.radar.presentation.mvi.RadarState
import com.codeskraps.umami.domain.AnalyticsRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RadarViewModel(
    private val radarRepository: RadarRepository,
    private val locationTracker: LocationTracker,
    private val localResource: LocalResourceRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val analyticsRepository: AnalyticsRepository,
) : StateReducerViewModel<RadarState, RadarEvent, RadarAction>(RadarState.initial) {

    private var animationJob: Job? = null

    override fun reduceState(currentState: RadarState, event: RadarEvent): RadarState {
        return when (event) {
            is RadarEvent.Resume -> onResume(currentState)
            is RadarEvent.RadarDataLoaded -> onRadarDataLoaded(currentState, event.frames)
            is RadarEvent.Error -> onError(currentState, event.message)
            is RadarEvent.PlayPause -> onPlayPause(currentState)
            is RadarEvent.SeekToFrame -> onSeekToFrame(currentState, event.index)
            is RadarEvent.NextFrame -> onNextFrame(currentState)
            is RadarEvent.LocationUpdated -> onLocationUpdated(currentState, event.location)
        }
    }

    private fun onResume(currentState: RadarState): RadarState {
        viewModelScope.launch(dispatcherProvider.io) {
            analyticsRepository.trackPageView("radar")

            locationTracker.getCurrentLocation()?.let {
                state.handleEvent(RadarEvent.LocationUpdated(LatLng(it.latitude, it.longitude)))
            }

            when (val result = radarRepository.getRadarFrames()) {
                is Resource.Success -> {
                    state.handleEvent(RadarEvent.RadarDataLoaded(result.data.allFrames))
                }
                is Resource.Error -> {
                    state.handleEvent(RadarEvent.Error(result.message))
                }
            }
        }
        return currentState.copy(isLoading = true, error = null)
    }

    private fun onRadarDataLoaded(currentState: RadarState, frames: List<RadarFrame>): RadarState {
        startAnimation()
        return currentState.copy(
            isLoading = false,
            radarFrames = frames,
            currentFrameIndex = 0,
            isPlaying = true,
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
            startAnimation()
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

    private fun startAnimation() {
        animationJob?.cancel()
        animationJob = viewModelScope.launch {
            while (true) {
                delay(500)
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
    }
}
