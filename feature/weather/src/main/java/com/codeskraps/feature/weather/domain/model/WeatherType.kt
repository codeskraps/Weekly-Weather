package com.codeskraps.feature.weather.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.codeskraps.feature.weather.R
import com.codeskraps.feature.weather.presentation.components.animations.AnimationDrawable
import com.codeskraps.feature.weather.presentation.components.animations.ClearDayAnimation
import com.codeskraps.feature.weather.presentation.components.animations.ClearNightAnimation
import com.codeskraps.feature.weather.presentation.components.animations.DenseDrizzleDayAnimation
import com.codeskraps.feature.weather.presentation.components.animations.DenseDrizzleNightAnimation
import com.codeskraps.feature.weather.presentation.components.animations.FoggyAnimation
import com.codeskraps.feature.weather.presentation.components.animations.FoggyDayAnimation
import com.codeskraps.feature.weather.presentation.components.animations.FoggyNightAnimation
import com.codeskraps.feature.weather.presentation.components.animations.FrostDayAnimation
import com.codeskraps.feature.weather.presentation.components.animations.NoAnimation
import com.codeskraps.feature.weather.presentation.components.animations.FrostNightAnimation
import com.codeskraps.feature.weather.presentation.components.animations.HeavyFreezingRainAnimation
import com.codeskraps.feature.weather.presentation.components.animations.HeavyRainAnimation
import com.codeskraps.feature.weather.presentation.components.animations.HeavySnowDayAnimation
import com.codeskraps.feature.weather.presentation.components.animations.HeavySnowNightAnimation
import com.codeskraps.feature.weather.presentation.components.animations.LightDrizzleDayAnimation
import com.codeskraps.feature.weather.presentation.components.animations.LightDrizzleNightAnimation
import com.codeskraps.feature.weather.presentation.components.animations.LightFreezingDrizzleAnimation
import com.codeskraps.feature.weather.presentation.components.animations.LightSnowAnimation
import com.codeskraps.feature.weather.presentation.components.animations.LightSnowDayAnimation
import com.codeskraps.feature.weather.presentation.components.animations.LightSnowNightAnimation
import com.codeskraps.feature.weather.presentation.components.animations.MainlyClearDayAnimation
import com.codeskraps.feature.weather.presentation.components.animations.MainlyClearNightAnimation
import com.codeskraps.feature.weather.presentation.components.animations.ModerateDrizzleDayAnimation
import com.codeskraps.feature.weather.presentation.components.animations.ModerateDrizzleNightAnimation
import com.codeskraps.feature.weather.presentation.components.animations.ModerateRainAnimation
import com.codeskraps.feature.weather.presentation.components.animations.ModerateSnowDayAnimation
import com.codeskraps.feature.weather.presentation.components.animations.ModerateSnowNightAnimation
import com.codeskraps.feature.weather.presentation.components.animations.ModerateThunderstormAnimation
import com.codeskraps.feature.weather.presentation.components.animations.ModerateThunderstormDayAnimation
import com.codeskraps.feature.weather.presentation.components.animations.ModerateThunderstormNightAnimation
import com.codeskraps.feature.weather.presentation.components.animations.OvercastAnimation
import com.codeskraps.feature.weather.presentation.components.animations.PartlyCloudyNightAnimation
import com.codeskraps.feature.weather.presentation.components.animations.PartlyCloudyDayAnimation
import com.codeskraps.feature.weather.presentation.components.animations.SlightRainAnimation
import com.codeskraps.feature.weather.presentation.components.animations.SnowGrainsAnimation

sealed class WeatherType(
    @param:StringRes val weatherDescRes: Int,
    @param:DrawableRes val iconRes: Int,
    val animation: AnimationDrawable
) {
    data object ClearSkyDay : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.clear_sky,
        iconRes = R.drawable.ic_clear_day,
        animation = ClearDayAnimation()
    )

    data object ClearSkyNight : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.clear_sky,
        iconRes = R.drawable.ic_clear_night,
        animation = ClearNightAnimation()
    )

    data object FrostDay : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.frost,
        iconRes = R.drawable.ic_frost_day,
        animation = FrostDayAnimation()
    )

    data object FrostNight : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.frost,
        iconRes = R.drawable.ic_frost_night,
        animation = FrostNightAnimation()
    )

    data object MainlyClearDay : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.mainly_clear,
        iconRes = R.drawable.ic_mainly_clear_day,
        animation = MainlyClearDayAnimation()
    )

    data object MainlyClearNight : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.mainly_clear,
        iconRes = R.drawable.ic_mainly_clear_night,
        animation = MainlyClearNightAnimation()
    )

    data object PartlyCloudyDay : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.partly_cloudy,
        iconRes = R.drawable.ic_partly_cloudy_day,
        animation = PartlyCloudyDayAnimation()
    )

    data object PartlyCloudyNight : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.partly_cloudy,
        iconRes = R.drawable.ic_partly_cloudy_night,
        animation = PartlyCloudyNightAnimation()
    )

    data object Overcast : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.overcast,
        iconRes = R.drawable.ic_overcast,
        animation = OvercastAnimation()
    )

    data object FoggyDay : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.foggy,
        iconRes = R.drawable.ic_fog_day,
        animation = FoggyDayAnimation()
    )

    data object FoggyNight : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.foggy,
        iconRes = R.drawable.ic_fog_night,
        animation = FoggyNightAnimation()
    )

    data object DepositingRimeFog : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.depositing_rime_fog,
        iconRes = R.drawable.ic_foggy,
        animation = FoggyAnimation()
    )

    data object LightDrizzleDay : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.light_drizzle,
        iconRes = R.drawable.ic_light_drizzel_day,
        animation = LightDrizzleDayAnimation()
    )

    data object LightDrizzleNight : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.light_drizzle,
        iconRes = R.drawable.ic_light_drizzel_night,
        animation = LightDrizzleNightAnimation()
    )

    data object ModerateDrizzleDay : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.moderate_drizzle,
        iconRes = R.drawable.ic_moderate_drizzel_day,
        animation = ModerateDrizzleDayAnimation()
    )

    data object ModerateDrizzleNight : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.moderate_drizzle,
        iconRes = R.drawable.ic_moderate_drizzel_night,
        animation = ModerateDrizzleNightAnimation()
    )

    data object DenseDrizzleDay : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.dense_drizzle,
        iconRes = R.drawable.ic_dense_drizzel_day,
        animation = DenseDrizzleDayAnimation()
    )

    data object DenseDrizzleNight : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.dense_drizzle,
        iconRes = R.drawable.ic_dense_drizzel_night,
        animation = DenseDrizzleNightAnimation()
    )

    data object LightFreezingDrizzle : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.slight_freezing_drizzle,
        iconRes = R.drawable.ic_snow_rain,
        animation = LightFreezingDrizzleAnimation()
    )

    data object DenseFreezingDrizzle : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.dense_freezing_drizzle,
        iconRes = R.drawable.ic_snow_rain,
        animation = LightFreezingDrizzleAnimation()
    )

    data object SlightRain : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.slight_rain,
        iconRes = R.drawable.ic_light_rain,
        animation = SlightRainAnimation()
    )

    data object ModerateRain : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.rainy,
        iconRes = R.drawable.ic_moderate_rain,
        animation = ModerateRainAnimation()
    )

    data object HeavyRain : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.heavy_rain,
        iconRes = R.drawable.ic_heavy_rain,
        animation = HeavyRainAnimation()
    )

    data object HeavyFreezingRain : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.heavy_freezing_rain,
        iconRes = R.drawable.ic_rain_sleet,
        animation = HeavyFreezingRainAnimation()
    )

    data object SlightSnowFallDay : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.slight_snow_fall,
        iconRes = R.drawable.ic_light_snow_day,
        animation = LightSnowDayAnimation()
    )

    data object SlightSnowFallNight : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.slight_snow_fall,
        iconRes = R.drawable.ic_light_snow_night,
        animation = LightSnowNightAnimation()
    )

    data object ModerateSnowFallDay : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.moderate_snow_fall,
        iconRes = R.drawable.ic_moderate_snow_day,
        animation = ModerateSnowDayAnimation()
    )

    data object ModerateSnowFallNight : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.moderate_snow_fall,
        iconRes = R.drawable.ic_moderate_snow_night,
        animation = ModerateSnowNightAnimation()
    )

    data object HeavySnowFallDay : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.heavy_snow_fall,
        iconRes = R.drawable.ic_heavy_snow_day,
        animation = HeavySnowDayAnimation()
    )

    data object HeavySnowFallNight : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.heavy_snow_fall,
        iconRes = R.drawable.ic_heavy_snow_night,
        animation = HeavySnowNightAnimation()
    )

    data object SnowGrains : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.snow_grains,
        iconRes = R.drawable.ic_heavy_snow,
        animation = SnowGrainsAnimation()
    )

    data object SlightRainShowers : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.slight_rain_showers,
        iconRes = R.drawable.ic_light_rain,
        animation = SlightRainAnimation()
    )

    data object ModerateRainShowers : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.moderate_rain_showers,
        iconRes = R.drawable.ic_moderate_rain,
        animation = ModerateRainAnimation()
    )

    data object ViolentRainShowers : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.violent_rain_showers,
        iconRes = R.drawable.ic_heavy_rain,
        animation = HeavyRainAnimation()
    )

    data object SlightSnowShowers : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.light_snow_showers,
        iconRes = R.drawable.ic_light_snow,
        animation = LightSnowAnimation()
    )

    data object HeavySnowShowers : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.heavy_snow_showers,
        iconRes = R.drawable.ic_heavy_snow,
        animation = SnowGrainsAnimation()
    )

    data object ModerateThunderstormDay : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.moderate_thunderstorm,
        iconRes = R.drawable.ic_thunderstorm_day,
        animation = ModerateThunderstormDayAnimation()
    )

    data object ModerateThunderstormNight : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.moderate_thunderstorm,
        iconRes = R.drawable.ic_thunderstorm_night,
        animation = ModerateThunderstormNightAnimation()
    )

    data object SlightHailThunderstorm : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.thunderstorm_with_slight_hail,
        iconRes = R.drawable.ic_thunderstorm,
        animation = ModerateThunderstormAnimation()
    )

    data object HeavyHailThunderstorm : WeatherType(
        weatherDescRes = com.codeskraps.feature.common.R.string.thunderstorm_with_heavy_rain,
        iconRes = R.drawable.ic_heavy_thunderstorm,
        animation = NoAnimation(R.drawable.ic_heavy_thunderstorm)
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