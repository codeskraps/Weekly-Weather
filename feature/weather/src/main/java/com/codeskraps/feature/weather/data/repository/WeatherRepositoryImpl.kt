package com.codeskraps.feature.weather.data.repository

import com.codeskraps.core.local.domain.repository.LocalResourceRepository
import com.codeskraps.feature.common.util.Resource
import com.codeskraps.feature.weather.data.mappers.toWeatherInfo
import com.codeskraps.feature.weather.data.remote.WeatherApi
import com.codeskraps.feature.weather.data.util.retryWithExponentialBackoff
import com.codeskraps.feature.weather.domain.model.WeatherInfo
import com.codeskraps.feature.weather.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    private val localResource: LocalResourceRepository
) : WeatherRepository {
    override suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo> {
        return try {
            Resource.Success(
                data = retryWithExponentialBackoff {
                    api.getWeatherData(
                        lat = lat,
                        long = long
                    ).toWeatherInfo()
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: localResource.getUnknownErrorString())
        }
    }
}