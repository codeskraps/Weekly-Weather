package com.example.weather.feature.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.core.domain.util.Resource
import com.example.weather.feature.common.dispatcher.DispatcherProvider
import com.example.weather.feature.common.mvi.StateReducerFlow
import com.example.weather.feature.weather.domain.location.LocationTracker
import com.example.weather.feature.weather.domain.model.WeatherInfo
import com.example.weather.feature.weather.domain.model.WeatherLocation
import com.example.weather.feature.weather.domain.repository.WeatherRepository
import com.example.weather.feature.weather.presentation.mvi.WeatherEvent
import com.example.weather.feature.weather.presentation.mvi.WeatherState
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
                                    geoLocation = intLocation.name
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

    private fun handleError(currentState: WeatherState, message: String): WeatherState {
        return currentState.copy(
            isLoading = false,
            error = message,
            weatherInfo = null
        )
    }
}