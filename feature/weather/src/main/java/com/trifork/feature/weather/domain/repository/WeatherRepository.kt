package com.trifork.feature.weather.domain.repository


import com.trifork.feature.common.util.Resource
import com.trifork.feature.weather.domain.model.WeatherInfo

interface WeatherRepository {

    suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo>
}