package com.codeskraps.feature.weather.data.mappers

import com.codeskraps.core.local.domain.model.GeoLocation
import com.codeskraps.feature.weather.data.remote.HourlyDto
import com.codeskraps.feature.weather.data.remote.SunDataDto
import com.codeskraps.feature.weather.data.remote.WeatherDto
import com.codeskraps.feature.weather.domain.model.SunData
import com.codeskraps.feature.weather.domain.model.WeatherData
import com.codeskraps.feature.weather.domain.model.WeatherInfo
import com.codeskraps.feature.weather.domain.model.WeatherLocation
import com.codeskraps.feature.weather.domain.model.WeatherType
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

fun HourlyDto.toWeatherDataMap(sunData: SunData): ImmutableMap<Int, ImmutableList<WeatherData>> {
    return time.mapIndexed { index, time ->
        val localDateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME)
        val weatherCode = weathercode[index]
        val sunrise = sunData.sunrise.first { it.dayOfMonth == localDateTime.dayOfMonth }
        val sunset = sunData.sunset.first { it.dayOfMonth == localDateTime.dayOfMonth }
        val isDay = isDay(localDateTime, sunrise, sunset)
        val temperature = temperature_2m[index]
        val isFreezing = isFreezing(temperature)

        IndexWeatherData(
            index = index,
            data = WeatherData(
                time = localDateTime,
                temperatureCelsius = temperature,
                pressure = pressure_msl[index],
                windSpeed = windspeed_10m[index],
                humidity = relativehumidity_2m[index],
                weatherType = WeatherType.fromWMO(
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

private fun SunDataDto.sunData(): SunData {
    return SunData(
        sunrise = sunrise.map { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) },
        sunset = sunset.map { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) },
    )
}

fun WeatherDto.toWeatherInfo(): WeatherInfo {
    val weatherDataMap = hourly.toWeatherDataMap(daily.sunData())
    val now = LocalDateTime.now()
    val currentWeatherData = weatherDataMap[0]?.minByOrNull { weatherData ->
        val timeDiff = kotlin.math.abs(weatherData.time.hour - now.hour)
        if (timeDiff > 12) 24 - timeDiff else timeDiff  // Handle cases around midnight
    }
    return WeatherInfo(
        geoLocation = "",
        weatherDataPerDay = weatherDataMap,
        currentWeatherData = currentWeatherData,
    )
}

fun WeatherInfo.toWeatherLocation(): WeatherLocation {
    return WeatherLocation(
        name = geoLocation,
        lat = latitude,
        long = longitude
    )
}

fun WeatherLocation.toGeoLocation(): GeoLocation {
    return GeoLocation(
        name = name,
        latitude = lat,
        longitude = long,
        country = "",
        admin1 = null
    )
}