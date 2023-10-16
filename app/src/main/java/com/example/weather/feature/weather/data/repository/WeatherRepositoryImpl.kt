package com.example.weather.feature.weather.data.repository

import com.example.weather.feature.weather.data.mappers.toWeatherInfo
import com.example.weather.feature.weather.data.remote.WeatherApi
import com.example.weather.feature.weather.domain.repository.WeatherRepository
import com.example.weather.core.domain.util.Resource
import com.example.weather.feature.weather.domain.model.WeatherInfo
import java.lang.Exception
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