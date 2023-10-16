package com.example.weather.feature.weather.domain.repository

import com.example.weather.core.domain.util.Resource
import com.example.weather.feature.weather.domain.model.WeatherInfo

interface WeatherRepository {

    suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo>
}