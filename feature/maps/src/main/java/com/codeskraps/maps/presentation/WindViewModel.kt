package com.codeskraps.maps.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.codeskraps.feature.common.dispatcher.DispatcherProvider
import com.codeskraps.feature.common.mvi.StateReducerViewModel
import com.codeskraps.feature.common.util.Resource
import com.codeskraps.maps.domain.repository.WindRepository
import com.codeskraps.maps.presentation.mvi.WindAction
import com.codeskraps.maps.presentation.mvi.WindEvent
import com.codeskraps.maps.presentation.mvi.WindState
import kotlinx.coroutines.launch

class WindViewModel(
    private val windRepository: WindRepository,
    private val dispatcherProvider: DispatcherProvider,
) : StateReducerViewModel<WindState, WindEvent, WindAction>(WindState.initial) {

    private var lastFetchTime: Long = 0
    private var lastBoundsKey: String = ""
    private companion object {
        const val TAG = "WeatherApp:WindVM"
        const val WIND_CACHE_DURATION = 5 * 60 * 1000L
    }

    override fun reduceState(currentState: WindState, event: WindEvent): WindState {
        return when (event) {
            is WindEvent.LoadWind -> onLoadWind(currentState, event)
            is WindEvent.WindDataLoaded -> currentState.copy(
                isLoading = false,
                windData = event.windData,
                error = null
            )
            is WindEvent.Error -> {
                viewModelScope.launch {
                    actionChannel.send(WindAction.ShowToast(event.message))
                }
                currentState.copy(isLoading = false, error = event.message)
            }
        }
    }

    private fun onLoadWind(currentState: WindState, event: WindEvent.LoadWind): WindState {
        val boundsKey = "%.1f,%.1f,%.1f,%.1f".format(event.latMin, event.latMax, event.lngMin, event.lngMax)
        Log.d(TAG, "LoadWind: $boundsKey")
        val now = System.currentTimeMillis()
        val boundsChanged = boundsKey != lastBoundsKey
        if (currentState.windData != null && !boundsChanged && now - lastFetchTime < WIND_CACHE_DURATION) {
            Log.d(TAG, "Using cached wind data (${currentState.windData.points.size} points)")
            return currentState
        }
        lastBoundsKey = boundsKey

        viewModelScope.launch(dispatcherProvider.io) {
            when (val result = windRepository.getWindGrid(
                latMin = event.latMin,
                latMax = event.latMax,
                lngMin = event.lngMin,
                lngMax = event.lngMax
            )) {
                is Resource.Success -> {
                    Log.d(TAG, "Wind data loaded: ${result.data.points.size} points")
                    lastFetchTime = System.currentTimeMillis()
                    state.handleEvent(WindEvent.WindDataLoaded(result.data))
                }
                is Resource.Error -> {
                    Log.e(TAG, "Wind data error: ${result.message}")
                    state.handleEvent(WindEvent.Error(result.message))
                }
            }
        }
        return currentState.copy(isLoading = true, error = null)
    }
}
