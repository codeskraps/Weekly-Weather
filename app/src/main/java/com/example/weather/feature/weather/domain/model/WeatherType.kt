package com.example.weather.feature.weather.domain.model

import androidx.annotation.DrawableRes
import com.example.weather.R

sealed class WeatherType(
    val weatherDesc: String,
    @DrawableRes val iconRes: Int
) {
    data object ClearSkyDay : WeatherType(
        weatherDesc = "Clear sky",
        iconRes = R.drawable.ic_clear_day
    )

    data object ClearSkyNight : WeatherType(
        weatherDesc = "Clear sky",
        iconRes = R.drawable.ic_clear_night
    )

    data object FrostDay : WeatherType(
        weatherDesc = "Frost",
        iconRes = R.drawable.ic_frost_day
    )

    data object FrostNight : WeatherType(
        weatherDesc = "Frost",
        iconRes = R.drawable.ic_frost_night
    )

    data object MainlyClearDay : WeatherType(
        weatherDesc = "Mainly clear",
        iconRes = R.drawable.ic_mainly_clear_day
    )

    data object MainlyClearNight : WeatherType(
        weatherDesc = "Mainly clear",
        iconRes = R.drawable.ic_mainly_clear_night
    )

    data object PartlyCloudyDay : WeatherType(
        weatherDesc = "Partly cloudy",
        iconRes = R.drawable.ic_partly_cloudy_day
    )

    data object PartlyCloudyNight : WeatherType(
        weatherDesc = "Partly cloudy",
        iconRes = R.drawable.ic_partly_cloudy_night
    )

    data object Overcast : WeatherType(
        weatherDesc = "Overcast",
        iconRes = R.drawable.ic_overcast
    )

    data object FoggyDay : WeatherType(
        weatherDesc = "Foggy",
        iconRes = R.drawable.ic_fog_day
    )

    data object FoggyNight : WeatherType(
        weatherDesc = "Foggy",
        iconRes = R.drawable.ic_fog_night
    )

    data object DepositingRimeFog : WeatherType(
        weatherDesc = "Depositing rime fog",
        iconRes = R.drawable.ic_foggy
    )

    data object LightDrizzleDay : WeatherType(
        weatherDesc = "Light drizzle",
        iconRes = R.drawable.ic_light_drizzel_day
    )

    data object LightDrizzleNight : WeatherType(
        weatherDesc = "Light drizzle",
        iconRes = R.drawable.ic_light_drizzel_night
    )

    data object ModerateDrizzleDay : WeatherType(
        weatherDesc = "Moderate drizzle",
        iconRes = R.drawable.ic_moderate_drizzel_day
    )

    data object ModerateDrizzleNight : WeatherType(
        weatherDesc = "Moderate drizzle",
        iconRes = R.drawable.ic_moderate_drizzel_night
    )

    data object DenseDrizzleDay : WeatherType(
        weatherDesc = "Dense drizzle",
        iconRes = R.drawable.ic_dense_drizzel_day
    )

    data object DenseDrizzleNight : WeatherType(
        weatherDesc = "Dense drizzle",
        iconRes = R.drawable.ic_dense_drizzel_night
    )

    data object LightFreezingDrizzle : WeatherType(
        weatherDesc = "Slight freezing drizzle",
        iconRes = R.drawable.ic_snow_rain
    )

    data object DenseFreezingDrizzle : WeatherType(
        weatherDesc = "Dense freezing drizzle",
        iconRes = R.drawable.ic_snow_rain
    )

    data object SlightRain : WeatherType(
        weatherDesc = "Slight rain",
        iconRes = R.drawable.ic_light_rain
    )

    data object ModerateRain : WeatherType(
        weatherDesc = "Rainy",
        iconRes = R.drawable.ic_moderate_rain
    )

    data object HeavyRain : WeatherType(
        weatherDesc = "Heavy rain",
        iconRes = R.drawable.ic_heavy_rain
    )

    data object HeavyFreezingRain : WeatherType(
        weatherDesc = "Heavy freezing rain",
        iconRes = R.drawable.ic_rain_sleet
    )

    data object SlightSnowFallDay : WeatherType(
        weatherDesc = "Slight snow fall",
        iconRes = R.drawable.ic_light_snow_day
    )

    data object SlightSnowFallNight : WeatherType(
        weatherDesc = "Slight snow fall",
        iconRes = R.drawable.ic_light_drizzel_night
    )

    data object ModerateSnowFallDay : WeatherType(
        weatherDesc = "Moderate snow fall",
        iconRes = R.drawable.ic_moderate_snow_day
    )

    data object ModerateSnowFallNight : WeatherType(
        weatherDesc = "Moderate snow fall",
        iconRes = R.drawable.ic_moderate_snow_night
    )

    data object HeavySnowFallDay : WeatherType(
        weatherDesc = "Heavy snow fall",
        iconRes = R.drawable.ic_heavy_snow_day
    )

    data object HeavySnowFallNight : WeatherType(
        weatherDesc = "Heavy snow fall",
        iconRes = R.drawable.ic_heavy_snow_night
    )

    data object SnowGrains : WeatherType(
        weatherDesc = "Snow grains",
        iconRes = R.drawable.ic_heavy_snow
    )

    data object SlightRainShowers : WeatherType(
        weatherDesc = "Slight rain showers",
        iconRes = R.drawable.ic_light_rain
    )

    data object ModerateRainShowers : WeatherType(
        weatherDesc = "Moderate rain showers",
        iconRes = R.drawable.ic_moderate_rain
    )

    data object ViolentRainShowers : WeatherType(
        weatherDesc = "Violent rain showers",
        iconRes = R.drawable.ic_heavy_rain
    )

    data object SlightSnowShowers : WeatherType(
        weatherDesc = "Light snow showers",
        iconRes = R.drawable.ic_light_snow
    )

    data object HeavySnowShowers : WeatherType(
        weatherDesc = "Heavy snow showers",
        iconRes = R.drawable.ic_heavy_snow
    )

    data object ModerateThunderstormDay : WeatherType(
        weatherDesc = "Moderate thunderstorm",
        iconRes = R.drawable.ic_thunderstorm_day
    )

    data object ModerateThunderstormNight : WeatherType(
        weatherDesc = "Moderate thunderstorm",
        iconRes = R.drawable.ic_thunderstorm_night
    )

    data object SlightHailThunderstorm : WeatherType(
        weatherDesc = "Thunderstorm with slight hail",
        iconRes = R.drawable.ic_thunderstorm
    )

    data object HeavyHailThunderstorm : WeatherType(
        weatherDesc = "Thunderstorm with heavy hail",
        iconRes = R.drawable.ic_heavy_thunderstorm
    )

    companion object {
        fun fromWMO(code: Int, isDay: Boolean, isFreezing: Boolean): WeatherType {
            return when (code) {
                1 -> if (isDay) MainlyClearDay else MainlyClearNight
                2 -> if (isDay) PartlyCloudyDay else PartlyCloudyNight
                3 -> Overcast
                45 -> if (isDay) FoggyDay else FoggyNight
                48 -> DepositingRimeFog
                51 -> if (isDay) LightDrizzleDay else LightDrizzleNight
                53 -> if (isDay) ModerateDrizzleDay else ModerateDrizzleNight
                55 -> if (isDay) DenseDrizzleDay else DenseDrizzleNight
                56 -> LightFreezingDrizzle
                57 -> DenseFreezingDrizzle
                61 -> SlightRain
                63 -> ModerateRain
                65 -> HeavyRain
                66 -> LightFreezingDrizzle
                67 -> HeavyFreezingRain
                71 -> if (isDay) SlightSnowFallDay else SlightSnowFallNight
                73 -> if (isDay) ModerateSnowFallDay else ModerateSnowFallNight
                75 -> if (isDay) HeavySnowFallDay else HeavySnowFallNight
                77 -> SnowGrains
                80 -> SlightRainShowers
                81 -> ModerateRainShowers
                82 -> ViolentRainShowers
                85 -> SlightSnowShowers
                86 -> HeavySnowShowers
                95 -> if (isDay) ModerateThunderstormDay else ModerateThunderstormNight
                96 -> SlightHailThunderstorm
                99 -> HeavyHailThunderstorm
                else -> if (isDay) {
                    if (isFreezing) FrostDay else ClearSkyDay
                } else if (isFreezing) FrostNight else ClearSkyNight
            }
        }
    }
}