package com.trifork.feature.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trifork.feature.common.dispatcher.DispatcherProvider
import com.trifork.feature.common.mvi.StateReducerFlow
import com.trifork.feature.common.util.Resource
import com.trifork.feature.weather.domain.location.LocationTracker
import com.trifork.feature.weather.domain.model.WeatherInfo
import com.trifork.feature.weather.domain.model.WeatherLocation
import com.trifork.feature.weather.domain.repository.WeatherRepository
import com.trifork.feature.weather.presentation.mvi.WeatherEvent
import com.trifork.feature.weather.presentation.mvi.WeatherState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    val state = StateReducerFlow(
        initialState = WeatherState.initial,
        reduceState = ::reduceState,
    )

    private fun reduceState(
        currentState: WeatherState,
        event: WeatherEvent
    ): WeatherState {
        return when (event) {
            is WeatherEvent.LoadWeatherInfo -> loadWeatherInfo(currentState, event.geoLocation)
            is WeatherEvent.UpdateHourlyInfo -> updateHourlyInfo(currentState, event.weatherInfo)
            is WeatherEvent.Refresh -> onRefresh(currentState)
            is WeatherEvent.Error -> handleError(currentState, event.message)
        }
    }

    private fun loadWeatherInfo(
        currentState: WeatherState,
        geoLocation: WeatherLocation
    ): WeatherState {
        viewModelScope.launch(dispatcherProvider.io) {
            val location = if (geoLocation.lat == 0.0 || geoLocation.long == 0.0) {
                locationTracker.getCurrentLocation()?.let {
                    WeatherLocation(
                        "Current Location",
                        it.latitude,
                        it.longitude
                    )
                }
            } else {
                geoLocation
            }

            location?.let { intLocation ->
                when (val result =
                    weatherRepository.getWeatherData(intLocation.lat, intLocation.long)) {
                    is Resource.Success -> {
                        state.handleEvent(
                            WeatherEvent.UpdateHourlyInfo(
                                result.data!!.copy(
                                    geoLocation = intLocation.name,
                                    latitude = intLocation.lat,
                                    longitude = intLocation.long
                                )
                            )
                        )
                    }

                    is Resource.Error -> {
                        state.handleEvent(WeatherEvent.Error("Check Internet"))
                    }
                }
            } ?: kotlin.run {
                state.handleEvent(WeatherEvent.Error("Check GPS"))
            }
        }
        return currentState.copy(
            isLoading = true,
            error = null,
            weatherInfo = null
        )
    }

    private fun updateHourlyInfo(
        currentState: WeatherState,
        weatherInfo: WeatherInfo
    ): WeatherState {
        return currentState.copy(
            isLoading = false,
            error = null,
            weatherInfo = weatherInfo
        )
    }

    private fun onRefresh(currentState: WeatherState): WeatherState {
        viewModelScope.launch(dispatcherProvider.io) {
            currentState.weatherInfo?.let { intLocation ->
                when (val result =
                    weatherRepository.getWeatherData(intLocation.latitude, intLocation.longitude)) {
                    is Resource.Success -> {
                        state.handleEvent(
                            WeatherEvent.UpdateHourlyInfo(
                                result.data!!.copy(
                                    geoLocation = intLocation.geoLocation,
                                    latitude = intLocation.latitude,
                                    longitude = intLocation.longitude
                                )
                            )
                        )
                    }

                    is Resource.Error -> {
                        state.handleEvent(WeatherEvent.Error("Check Internet"))
                    }
                }
            } ?: kotlin.run {
                state.handleEvent(WeatherEvent.Error("Check GPS"))
            }
        }
        return currentState.copy(
            isLoading = false,
            error = null,
            weatherInfo = currentState.weatherInfo
        )
    }

    private fun handleError(currentState: WeatherState, message: String): WeatherState {
        return currentState.copy(
            isLoading = false,
            error = message,
            weatherInfo = null
        )
    }
}