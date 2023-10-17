package com.trifork.feature.weather.data.repository

import com.trifork.feature.common.util.Resource
import com.trifork.feature.weather.data.mappers.toWeatherInfo
import com.trifork.feature.weather.data.remote.WeatherApi
import com.trifork.feature.weather.domain.model.WeatherInfo
import com.trifork.feature.weather.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
) : WeatherRepository {
    override suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo> {
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