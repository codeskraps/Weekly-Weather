package com.codeskraps.feature.weather.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codeskraps.feature.common.util.WeekString
import com.codeskraps.feature.weather.domain.model.WeatherData
import com.codeskraps.feature.weather.domain.model.WeatherInfo
import com.codeskraps.feature.weather.presentation.mvi.WeatherEvent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Composable
fun WeatherForecast(
    weatherInfo: WeatherInfo,
    perDay: ImmutableList<WeatherData>,
    handleEvent: (WeatherEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val resource = LocalContext.current.resources

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        val today = perDay[0].time
        val min = perDay.minBy { it.temperatureCelsius }
        val max = perDay.maxBy { it.temperatureCelsius }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "${
                    resource.getString(
                        WeekString.parse(
                            today.dayOfWeek.value
                        )
                    )
                } ${today.dayOfMonth}",
                modifier = Modifier.align(Alignment.Bottom),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "${max.temperatureCelsius}${weatherInfo.temperatureUnit} - ${min.temperatureCelsius}${weatherInfo.temperatureUnit}",
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Bottom),
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        val listState = rememberLazyListState()
        val density = LocalDensity.current

        LazyRow(
            state = listState
        ) {
            val filteredData = perDay.filter { it.time.plusHours(1) > LocalDateTime.now() }
            // If all data points would be filtered out, keep the most recent one
            val displayData = if (filteredData.isEmpty()) listOf(perDay.maxBy { it.time }) else filteredData
            items(displayData) { weatherData ->
                HourlyWeatherDisplay(
                    weatherData = weatherData,
                    temperatureUnit = weatherInfo.temperatureUnit,
                    modifier = Modifier
                        .height(100.dp)
                        .padding(horizontal = 16.dp)
                        .clickable {
                            handleEvent(
                                WeatherEvent.UpdateHourlyInfo(
                                    WeatherInfo(
                                        geoLocation = weatherInfo.geoLocation,
                                        weatherDataPerDay = weatherInfo.weatherDataPerDay,
                                        currentWeatherData = weatherData,
                                        temperatureUnit = weatherInfo.temperatureUnit,
                                        windSpeedUnit = weatherInfo.windSpeedUnit,
                                    )
                                )
                            )
                        }
                )
            }
        }

        if (perDay[0].time.dayOfMonth != LocalDateTime.now().dayOfMonth) {
            LaunchedEffect(key1 = perDay) {
                launch {
                    delay(500)
                    val componentWidth = with(density) { (40.dp.roundToPx() + 32.dp.roundToPx()).toFloat() }
                    listState.animateScrollBy(
                        value = componentWidth * 11,
                        animationSpec = tween(3000, easing = FastOutSlowInEasing)
                    )
                    listState.animateScrollToItem(10)
                }
            }
        }
    }
}