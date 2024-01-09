package com.trifork.feature.weather.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.trifork.feature.weather.R

sealed class WeatherType(
    @StringRes val weatherDescRes: Int,
    @DrawableRes val iconRes: Int
) {
    data object ClearSkyDay : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.clear_sky,
        iconRes = R.drawable.ic_clear_day
    )

    data object ClearSkyNight : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.clear_sky,
        iconRes = R.drawable.ic_clear_night
    )

    data object FrostDay : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.frost,
        iconRes = R.drawable.ic_frost_day
    )

    data object FrostNight : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.frost,
        iconRes = R.drawable.ic_frost_night
    )

    data object MainlyClearDay : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.mainly_clear,
        iconRes = R.drawable.ic_mainly_clear_day
    )

    data object MainlyClearNight : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.mainly_clear,
        iconRes = R.drawable.ic_mainly_clear_night
    )

    data object PartlyCloudyDay : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.partly_cloudy,
        iconRes = R.drawable.ic_partly_cloudy_day
    )

    data object PartlyCloudyNight : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.partly_cloudy,
        iconRes = R.drawable.ic_partly_cloudy_night
    )

    data object Overcast : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.overcast,
        iconRes = R.drawable.ic_overcast
    )

    data object FoggyDay : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.foggy,
        iconRes = R.drawable.ic_fog_day
    )

    data object FoggyNight : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.foggy,
        iconRes = R.drawable.ic_fog_night
    )

    data object DepositingRimeFog : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.depositing_rime_fog,
        iconRes = R.drawable.ic_foggy
    )

    data object LightDrizzleDay : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.light_drizzle,
        iconRes = R.drawable.ic_light_drizzel_day
    )

    data object LightDrizzleNight : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.light_drizzle,
        iconRes = R.drawable.ic_light_drizzel_night
    )

    data object ModerateDrizzleDay : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.moderate_drizzle,
        iconRes = R.drawable.ic_moderate_drizzel_day
    )

    data object ModerateDrizzleNight : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.moderate_drizzle,
        iconRes = R.drawable.ic_moderate_drizzel_night
    )

    data object DenseDrizzleDay : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.dense_drizzle,
        iconRes = R.drawable.ic_dense_drizzel_day
    )

    data object DenseDrizzleNight : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.dense_drizzle,
        iconRes = R.drawable.ic_dense_drizzel_night
    )

    data object LightFreezingDrizzle : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.slight_freezing_drizzle,
        iconRes = R.drawable.ic_snow_rain
    )

    data object DenseFreezingDrizzle : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.dense_freezing_drizzle,
        iconRes = R.drawable.ic_snow_rain
    )

    data object SlightRain : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.slight_rain,
        iconRes = R.drawable.ic_light_rain
    )

    data object ModerateRain : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.rainy,
        iconRes = R.drawable.ic_moderate_rain
    )

    data object HeavyRain : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.heavy_rain,
        iconRes = R.drawable.ic_heavy_rain
    )

    data object HeavyFreezingRain : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.heavy_freezing_rain,
        iconRes = R.drawable.ic_rain_sleet
    )

    data object SlightSnowFallDay : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.slight_snow_fall,
        iconRes = R.drawable.ic_light_snow_day
    )

    data object SlightSnowFallNight : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.slight_snow_fall,
        iconRes = R.drawable.ic_light_drizzel_night
    )

    data object ModerateSnowFallDay : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.moderate_snow_fall,
        iconRes = R.drawable.ic_moderate_snow_day
    )

    data object ModerateSnowFallNight : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.moderate_snow_fall,
        iconRes = R.drawable.ic_moderate_snow_night
    )

    data object HeavySnowFallDay : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.heavy_snow_fall,
        iconRes = R.drawable.ic_heavy_snow_day
    )

    data object HeavySnowFallNight : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.heavy_snow_fall,
        iconRes = R.drawable.ic_heavy_snow_night
    )

    data object SnowGrains : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.snow_grains,
        iconRes = R.drawable.ic_heavy_snow
    )

    data object SlightRainShowers : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.slight_rain_showers,
        iconRes = R.drawable.ic_light_rain
    )

    data object ModerateRainShowers : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.moderate_rain_showers,
        iconRes = R.drawable.ic_moderate_rain
    )

    data object ViolentRainShowers : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.violent_rain_showers,
        iconRes = R.drawable.ic_heavy_rain
    )

    data object SlightSnowShowers : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.light_snow_showers,
        iconRes = R.drawable.ic_light_snow
    )

    data object HeavySnowShowers : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.heavy_snow_showers,
        iconRes = R.drawable.ic_heavy_snow
    )

    data object ModerateThunderstormDay : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.moderate_thunderstorm,
        iconRes = R.drawable.ic_thunderstorm_day
    )

    data object ModerateThunderstormNight : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.moderate_thunderstorm,
        iconRes = R.drawable.ic_thunderstorm_night
    )

    data object SlightHailThunderstorm : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.thunderstorm_with_slight_hail,
        iconRes = R.drawable.ic_thunderstorm
    )

    data object HeavyHailThunderstorm : WeatherType(
        weatherDescRes = com.trifork.feature.common.R.string.thunderstorm_with_heavy_rain,
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