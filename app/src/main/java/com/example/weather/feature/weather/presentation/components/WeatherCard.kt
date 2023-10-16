package com.example.weather.feature.weather.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.R
import com.example.weather.feature.weather.domain.model.WeatherData
import com.example.weather.feature.weather.domain.model.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Preview
@Composable
fun WeatherCardPreview() {
    WeatherCard(
        data = WeatherData(
            time = LocalDateTime.now(),
            temperatureCelsius = 24.2,
            pressure = 1022.toDouble(),
            windSpeed = 6.toDouble(),
            humidity = 62.toDouble(),
            weatherType = WeatherType.fromWMO(code = 0, isDay = true, isFreezing = false),
            sunrise = LocalDateTime.now(),
            sunset = LocalDateTime.now()
        ),
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier
    )
}

@Composable
fun WeatherCard(
    data: WeatherData,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${data.time.dayOfMonth} ${data.time.month.toString().lowercase()} ${
                    data.time.format(
                        DateTimeFormatter.ofPattern("HH:mm")
                    )
                }",
                modifier = Modifier.align(Alignment.End),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = data.weatherType.iconRes),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "${data.temperatureCelsius}°C",
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = data.weatherType.weatherDesc,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherDataDisplay(
                    value = data.sunrise.format(
                        DateTimeFormatter.ofPattern("HH:mm")
                    ),
                    icon = ImageVector.vectorResource(id = R.drawable.ic_sunrise),
                    iconTint = MaterialTheme.colorScheme.primary,
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.primary)
                )
                WeatherDataDisplay(
                    value = data.sunset.format(
                        DateTimeFormatter.ofPattern("HH:mm")
                    ),
                    icon = ImageVector.vectorResource(id = R.drawable.ic_moonrise),
                    iconTint = MaterialTheme.colorScheme.primary,
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
                    imageFirst = false
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeatherDataDisplay(
                    value = "${data.pressure.roundToInt()}hpa",
                    icon = ImageVector.vectorResource(id = R.drawable.ic_pressure),
                    iconTint = MaterialTheme.colorScheme.primary,
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.primary)
                )
                WeatherDataDisplay(
                    value = "${data.humidity.roundToInt()}%",
                    icon = ImageVector.vectorResource(id = R.drawable.ic_drop),
                    iconTint = MaterialTheme.colorScheme.primary,
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.primary)
                )
                WeatherDataDisplay(
                    value = "${data.windSpeed.roundToInt()}km/h",
                    icon = ImageVector.vectorResource(id = R.drawable.ic_wind),
                    iconTint = MaterialTheme.colorScheme.primary,
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}