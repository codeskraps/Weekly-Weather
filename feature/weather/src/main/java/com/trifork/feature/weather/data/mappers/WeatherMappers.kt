package com.trifork.feature.weather.data.mappers

import com.trifork.feature.weather.data.remote.SunDataDto
import com.trifork.feature.weather.data.remote.WeatherDataDto
import com.trifork.feature.weather.data.remote.WeatherDto
import com.trifork.feature.weather.domain.model.SunData
import com.trifork.feature.weather.domain.model.WeatherData
import com.trifork.feature.weather.domain.model.WeatherInfo
import com.trifork.feature.weather.domain.model.WeatherType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private data class IndexWeatherData(
    val index: Int,
    val data: com.trifork.feature.weather.domain.model.WeatherData
)

fun WeatherDataDto.toWeatherDataMap(sunData: com.trifork.feature.weather.domain.model.SunData): ImmutableMap<Int, ImmutableList<com.trifork.feature.weather.domain.model.WeatherData>> {
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
            data = com.trifork.feature.weather.domain.model.WeatherData(
                time = localDateTime,
                temperatureCelsius = temperature,
                pressure = pressures[index],
                windSpeed = windSpeeds[index],
                humidity = humidities[index],
                weatherType = com.trifork.feature.weather.domain.model.WeatherType.fromWMO(
                    weatherCode,
                    isDay,
                    isFreezing
                ),
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

private fun SunDataDto.sunData(): com.trifork.feature.weather.domain.model.SunData {
    return com.trifork.feature.weather.domain.model.SunData(
        sunrise = sunrise.map { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) },
        sunset = sunset.map { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) },
    )
}

fun WeatherDto.toWeatherInfo(): com.trifork.feature.weather.domain.model.WeatherInfo {
    val weatherDataMap = weatherData.toWeatherDataMap(sunData.sunData())
    val now = LocalDateTime.now()
    val currentWeatherData = weatherDataMap[0]?.find {
        val hour = if (now.minute < 30) now.hour else now.hour + 1
        it.time.hour == hour
    }
    return com.trifork.feature.weather.domain.model.WeatherInfo(
        geoLocation = "",
        weatherDataPerDay = weatherDataMap,
        currentWeatherData = currentWeatherData
    )
}