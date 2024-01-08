package com.trifork.feature.weather.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.trifork.feature.common.dispatcher.DispatcherProvider
import com.trifork.feature.common.domain.repository.LocalGeocodingRepository
import com.trifork.feature.common.mvi.StateReducerViewModel
import com.trifork.feature.common.util.Resource
import com.trifork.feature.weather.data.mappers.toGeoLocation
import com.trifork.feature.weather.data.mappers.toWeatherLocation
import com.trifork.feature.weather.domain.location.LocationTracker
import com.trifork.feature.weather.domain.model.WeatherInfo
import com.trifork.feature.weather.domain.model.WeatherLocation
import com.trifork.feature.weather.domain.repository.WeatherRepository
import com.trifork.feature.weather.presentation.mvi.WeatherAction
import com.trifork.feature.weather.presentation.mvi.WeatherEvent
import com.trifork.feature.weather.presentation.mvi.WeatherState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val localGeocodingRepository: LocalGeocodingRepository,
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker,
    private val dispatcherProvider: DispatcherProvider,
    private val savedStateHandle: SavedStateHandle
) : StateReducerViewModel<WeatherState, WeatherEvent, WeatherAction>() {

    override fun initState(): WeatherState = WeatherState.initial

    override fun reduceState(
        currentState: WeatherState,
        event: WeatherEvent
    ): WeatherState {
        Log.v("WeatherViewModel", "event: $event, state: ${state.value.weatherInfo?.geoLocation}")
        return when (event) {
            is WeatherEvent.LoadWeatherInfo -> onLoadWeatherInfo(currentState, event.geoLocation)
            is WeatherEvent.UpdateHourlyInfo -> onUpdateHourlyInfo(currentState, event.weatherInfo)
            is WeatherEvent.Refresh -> onRefresh(currentState)
            is WeatherEvent.Error -> handleError(currentState, event.message)
            is WeatherEvent.About -> currentState
            is WeatherEvent.CheckCache -> onCheckCached(currentState, event.weatherLocation)
            is WeatherEvent.IsCache -> onIsCached(currentState, event.isCached)
            is WeatherEvent.Resume -> onResume(currentState)
            is WeatherEvent.Save -> onSave(currentState, event.weatherLocation)
            is WeatherEvent.Delete -> onDelete(currentState, event.weatherLocation)
        }
    }

    private fun onLoadWeatherInfo(
        currentState: WeatherState,
        geoLocation: WeatherLocation
    ): WeatherState {
        viewModelScope.launch(dispatcherProvider.io) {
            val location = if (geoLocation.lat == .0 || geoLocation.long == .0) {
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
                        val weatherInfo = result.data.copy(
                            geoLocation = intLocation.name,
                            latitude = intLocation.lat,
                            longitude = intLocation.long
                        )

                        state.handleEvent(WeatherEvent.UpdateHourlyInfo(weatherInfo))
                        state.handleEvent(WeatherEvent.CheckCache(weatherInfo.toWeatherLocation()))
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

    private fun onUpdateHourlyInfo(
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
                        val weatherInfo = result.data.copy(
                            geoLocation = intLocation.geoLocation,
                            latitude = intLocation.latitude,
                            longitude = intLocation.longitude
                        )

                        state.handleEvent(WeatherEvent.UpdateHourlyInfo(weatherInfo))
                        state.handleEvent(WeatherEvent.CheckCache(weatherInfo.toWeatherLocation()))
                    }

                    is Resource.Error -> {
                        state.handleEvent(WeatherEvent.Error("Check Internet"))
                    }
                }
            }
        }
        return currentState.copy(
            isLoading = true,
            error = null,
            weatherInfo = currentState.weatherInfo
        )
    }

    private fun onCheckCached(
        currentState: WeatherState,
        weatherLocation: WeatherLocation
    ): WeatherState {
        viewModelScope.launch(dispatcherProvider.io) {
            val result =
                localGeocodingRepository.isCached(weatherLocation.lat, weatherLocation.long)
            when (result) {
                is Resource.Error -> state.handleEvent(WeatherEvent.IsCache(false))
                is Resource.Success -> state.handleEvent(WeatherEvent.IsCache(result.data))
            }
        }
        return currentState
    }

    private fun onIsCached(currentState: WeatherState, isCached: Boolean): WeatherState {
        return currentState.copy(cached = isCached)
    }

    private fun onResume(currentState: WeatherState): WeatherState {
        try {
            val name: String = savedStateHandle.get<String>("name") ?: "Current Location"
            val lat: String = savedStateHandle.get<String>("lat") ?: ".0"
            val long: String = savedStateHandle.get<String>("long") ?: ".0"

            val weatherLocation = WeatherLocation(
                name = name,
                lat = lat.toDouble(),
                long = long.toDouble()
            )

            state.handleEvent(WeatherEvent.LoadWeatherInfo(weatherLocation))
        } catch (e: Exception) {
            state.handleEvent(WeatherEvent.LoadWeatherInfo(WeatherLocation()))
        }
        return currentState
    }

    private fun onSave(currentState: WeatherState, weatherLocation: WeatherLocation): WeatherState {
        viewModelScope.launch(dispatcherProvider.io) {
            if (weatherLocation.name.isBlank()) {
                actionChannel.send(WeatherAction.Toast("Location can't be blank !!!"))
            } else {
                var name = weatherLocation.name.trim()

                if (name.endsWith(",")) {
                    name = name.substring(0, name.lastIndexOf(","))
                }

                when (val result =
                    localGeocodingRepository.saveCacheGeoLocation(
                        weatherLocation.copy(name = name).toGeoLocation()
                    )) {
                    is Resource.Success -> {
                        state.handleEvent(WeatherEvent.LoadWeatherInfo(weatherLocation))
                        state.handleEvent(WeatherEvent.CheckCache(weatherLocation))
                    }

                    is Resource.Error -> actionChannel.send(WeatherAction.Toast(result.message))
                }
            }
        }
        return currentState
    }

    private fun onDelete(
        currentState: WeatherState,
        weatherLocation: WeatherLocation
    ): WeatherState {
        viewModelScope.launch(dispatcherProvider.io) {
            when (val result =
                localGeocodingRepository.deleteCacheGeoLocation(weatherLocation.toGeoLocation())) {
                is Resource.Success -> state.handleEvent(WeatherEvent.CheckCache(weatherLocation))
                is Resource.Error -> actionChannel.send(WeatherAction.Toast(result.message))
            }
        }
        return currentState
    }

    private fun handleError(currentState: WeatherState, message: String): WeatherState {
        return currentState.copy(
            isLoading = false,
            error = message,
            weatherInfo = null
        )
    }
}