package com.trifork.feature.weather.data.repository

import com.trifork.feature.common.util.Resource
import com.trifork.feature.weather.data.mappers.toWeatherInfo
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: com.trifork.feature.weather.data.remote.WeatherApi
) : com.trifork.feature.weather.domain.repository.WeatherRepository {
    override suspend fun getWeatherData(lat: Double, long: Double): Resource<com.trifork.feature.weather.domain.model.WeatherInfo> {
        return try {
            Resource.Success(
                data = api.getWeatherData(
                    lat = lat,
                    long = long
                ).toWeatherInfo()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}