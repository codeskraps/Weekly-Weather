package com.codeskraps.feature.weather.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.codeskraps.feature.common.dispatcher.DispatcherProvider
import com.codeskraps.core.local.domain.repository.LocalGeocodingRepository
import com.codeskraps.core.local.domain.repository.LocalResourceRepository
import com.codeskraps.feature.common.mvi.StateReducerViewModel
import com.codeskraps.feature.common.util.Resource
import com.codeskraps.feature.weather.data.mappers.toGeoLocation
import com.codeskraps.feature.weather.data.mappers.toWeatherLocation
import com.codeskraps.feature.weather.domain.model.WeatherInfo
import com.codeskraps.feature.weather.domain.model.WeatherLocation
import com.codeskraps.feature.weather.domain.repository.WeatherRepository
import com.codeskraps.feature.weather.presentation.mvi.WeatherAction
import com.codeskraps.feature.weather.presentation.mvi.WeatherEvent
import com.codeskraps.feature.weather.presentation.mvi.WeatherState
import com.codeskraps.core.location.domain.LocationTracker
import com.codeskraps.umami.domain.AnalyticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val localGeocodingRepository: LocalGeocodingRepository,
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker,
    private val localResource: LocalResourceRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val savedStateHandle: SavedStateHandle,
    private val analyticsRepository: AnalyticsRepository
) : StateReducerViewModel<WeatherState, WeatherEvent, WeatherAction>(WeatherState.initial) {

    private companion object {
        private const val ANALYTICS_WEATHER_LOAD = "weather_load"
        private const val ANALYTICS_WEATHER_REFRESH = "weather_refresh"
        private const val ANALYTICS_LOCATION_SAVE = "location_save"
        private const val ANALYTICS_LOCATION_DELETE = "location_delete"
        private const val ANALYTICS_ERROR = "weather_error"
        
        private const val PARAM_LOCATION = "location"
        private const val PARAM_ERROR = "error_message"
        private const val PARAM_IS_CURRENT_LOCATION = "is_current_location"
    }

    private var currentLocationString: String = ""

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            currentLocationString = localResource.getCurrentLocationString()
        }
    }

    override fun reduceState(
        currentState: WeatherState,
        event: WeatherEvent
    ): WeatherState {
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
            val isCurrentLocation = geoLocation.lat == .0 || geoLocation.long == .0
            
            // Track page view for location change
            analyticsRepository.trackPageView("weather/${geoLocation.name}")
            
            analyticsRepository.trackEvent(
                ANALYTICS_WEATHER_LOAD,
                mapOf(
                    PARAM_LOCATION to geoLocation.name,
                    PARAM_IS_CURRENT_LOCATION to isCurrentLocation.toString()
                )
            )
            
            val location = if (isCurrentLocation) {
                locationTracker.getCurrentLocation()?.let {
                    savedStateHandle.run {
                        remove<String>("name")
                        remove<String>("lat")
                        remove<String>("long")
                    }

                    WeatherLocation(
                        localResource.getCurrentLocationString(),
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
                        state.handleEvent(WeatherEvent.Error(localResource.getCheckInternetString()))
                    }
                }
            } ?: kotlin.run {
                state.handleEvent(WeatherEvent.Error(localResource.getCheckGPSString()))
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
                analyticsRepository.trackEvent(
                    ANALYTICS_WEATHER_REFRESH,
                    mapOf(PARAM_LOCATION to intLocation.geoLocation)
                )
                
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
                        state.handleEvent(WeatherEvent.Error(localResource.getCheckInternetString()))
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
            // Track initial page view when screen resumes
            viewModelScope.launch(dispatcherProvider.io) {
                analyticsRepository.trackPageView("weather")
            }
            
            val name: String =
                savedStateHandle.get<String>("name") ?: currentLocationString
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
                actionChannel.send(WeatherAction.Toast(localResource.getLocationCanNotBeBlankString()))
            } else {
                var name = weatherLocation.name.trim()

                if (name.endsWith(",")) {
                    name = name.substring(0, name.lastIndexOf(","))
                }

                analyticsRepository.trackEvent(
                    ANALYTICS_LOCATION_SAVE,
                    mapOf(PARAM_LOCATION to name)
                )

                when (val result =
                    localGeocodingRepository.saveCacheGeoLocation(
                        weatherLocation.copy(name = name).toGeoLocation()
                    )) {
                    is Resource.Success -> {
                        savedStateHandle["name"] = name
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
            analyticsRepository.trackEvent(
                ANALYTICS_LOCATION_DELETE,
                mapOf(PARAM_LOCATION to weatherLocation.name)
            )
            
            when (val result =
                localGeocodingRepository.deleteCacheGeoLocation(weatherLocation.toGeoLocation())) {
                is Resource.Success -> state.handleEvent(WeatherEvent.CheckCache(weatherLocation))
                is Resource.Error -> actionChannel.send(WeatherAction.Toast(result.message))
            }
        }
        return currentState
    }

    private fun handleError(currentState: WeatherState, message: String): WeatherState {
        viewModelScope.launch(dispatcherProvider.io) {
            analyticsRepository.trackEvent(
                ANALYTICS_ERROR,
                mapOf(PARAM_ERROR to message)
            )
        }
        return currentState.copy(
            isLoading = false,
            error = message,
            weatherInfo = null
        )
    }
}