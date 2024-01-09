package com.trifork.feature.weather.presentation.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.trifork.feature.common.R
import com.trifork.feature.weather.data.mappers.toWeatherLocation
import com.trifork.feature.weather.domain.model.WeatherInfo
import com.trifork.feature.weather.presentation.mvi.WeatherEvent

@Composable
fun SaveLocationDialog(
    weatherInfo: WeatherInfo,
    showDialog: Boolean,
    handleEvent: (WeatherEvent) -> Unit,
    onDismissRequest: () -> Unit
) {
    if (showDialog) {
        val resources = LocalContext.current.resources
        if (weatherInfo.geoLocation == resources.getString(R.string.current_location)) {
            var nameLocation by remember {
                mutableStateOf(weatherInfo.geoLocation)
            }

            AlertDialog(
                onDismissRequest = onDismissRequest,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                title = {
                    Text(text = resources.getString(R.string.save_current_location))
                },
                text = {
                    Column {
                        Text(resources.getString(R.string.set_name_current_location))
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = nameLocation,
                            onValueChange = { nameLocation = it })
                    }
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = {
                            handleEvent(
                                WeatherEvent.Save(
                                    weatherInfo.copy(geoLocation = nameLocation).toWeatherLocation()
                                )
                            )
                            onDismissRequest()
                        }
                    ) {
                        Text(resources.getString(R.string.save))
                    }
                },
                dismissButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
                        onClick = onDismissRequest
                    ) {
                        Text(resources.getString(R.string.cancel))
                    }
                }
            )
        } else {
            handleEvent(WeatherEvent.Save(weatherInfo.toWeatherLocation()))
            onDismissRequest()
        }
    }
}