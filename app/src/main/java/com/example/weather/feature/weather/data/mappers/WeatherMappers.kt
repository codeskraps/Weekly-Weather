package com.example.weather.feature.weather.data.mappers

import com.example.weather.feature.weather.data.remote.SunDataDto
import com.example.weather.feature.weather.data.remote.WeatherDataDto
import com.example.weather.feature.weather.data.remote.WeatherDto
import com.example.weather.feature.weather.domain.model.SunData
import com.example.weather.feature.weather.domain.model.WeatherData
import com.example.weather.feature.weather.domain.model.WeatherInfo
import com.example.weather.feature.weather.domain.model.WeatherType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private data class IndexWeatherData(
    val index: Int,
    val data: WeatherData
)

fun WeatherDataDto.toWeatherDataMap(sunData: SunData): ImmutableMap<Int, ImmutableList<WeatherData>> {
    return time.mapIndexed { index, time ->
        val localDateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME)
        val weatherCode = weatherCodes[index]
        val sunrise = sunData.sunrise.first { it.dayOfMonth == localDateTime.dayOfMonth }
        val sunset = sunData.sunset.first { it.dayOfMonth == localDateTime.dayOfMonth }
        val isDay = isDay(localDateTime, sunrise, sunset)
        val temperature = temperatures[index]
        val isFreezing = isFreezing(temperature)

        IndexWeatherData(
            index = index,
            data = WeatherData(
                time = localDateTime,
                temperatureCelsius = temperature,
                pressure = pressures[index],
                windSpeed = windSpeeds[index],
                humidity = humidities[index],
                weatherType = WeatherType.fromWMO(weatherCode, isDay, isFreezing),
                sunrise = sunrise,
                sunset = sunset
            )
        )
    }.groupBy {
        it.index / 24
    }.mapValues { map ->
        map.value.map { it.data }.toImmutableList()
    }.toImmutableMap()
}

private fun isDay(time: LocalDateTime, sunrise: LocalDateTime, sunset: LocalDateTime): Boolean {
    return time.isAfter(sunrise) && time.isBefore(sunset)
}

private fun isFreezing(temperature: Double): Boolean {
    return temperature < 0
}

private fun SunDataDto.sunData(): SunData {
    return SunData(
        sunrise = sunrise.map { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) },
        sunset = sunset.map { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) },
    )
}

fun WeatherDto.toWeatherInfo(): WeatherInfo {
    val weatherDataMap = weatherData.toWeatherDataMap(sunData.sunData())
    val now = LocalDateTime.now()
    val currentWeatherData = weatherDataMap[0]?.find {
        val hour = if (now.minute < 30) now.hour else now.hour + 1
        it.time.hour == hour
    }
    return WeatherInfo(
        geoLocation = "",
        weatherDataPerDay = weatherDataMap,
        currentWeatherData = currentWeatherData
    )
}