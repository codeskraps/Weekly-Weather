package com.trifork.feature.weather.presentation.components

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trifork.feature.common.util.MonthString
import com.trifork.feature.common.util.WeekString
import com.trifork.feature.weather.domain.model.WeatherData
import com.trifork.feature.weather.domain.model.WeatherInfo
import com.trifork.feature.weather.presentation.WeatherViewModel
import com.trifork.feature.weather.presentation.mvi.WeatherEvent
import kotlinx.collections.immutable.ImmutableList
import java.time.LocalDateTime
import java.util.Locale

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
                text = "${max.temperatureCelsius}°C - ${min.temperatureCelsius}°C",
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Bottom),
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(content = {
            items(perDay.filter { it.time.plusHours(1) > LocalDateTime.now() }) { weatherData ->
                HourlyWeatherDisplay(
                    weatherData = weatherData,
                    modifier = Modifier
                        .height(100.dp)
                        .padding(horizontal = 16.dp)
                        .clickable {
                            handleEvent(
                                WeatherEvent.UpdateHourlyInfo(
                                    WeatherInfo(
                                        geoLocation = weatherInfo.geoLocation,
                                        weatherDataPerDay = weatherInfo.weatherDataPerDay,
                                        currentWeatherData = weatherData
                                    )
                                )
                            )
                        }
                )
            }
        })
    }
}