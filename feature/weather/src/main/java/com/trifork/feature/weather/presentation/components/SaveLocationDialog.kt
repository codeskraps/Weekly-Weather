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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        if (weatherInfo.geoLocation == "Current Location") {
            var nameLocation by remember {
                mutableStateOf(weatherInfo.geoLocation)
            }

            AlertDialog(
                onDismissRequest = onDismissRequest,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                title = {
                    Text(text = "Save Current Location")
                },
                text = {
                    Column {
                        Text("Set a name for the current location:")
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = nameLocation,
                            onValueChange = { nameLocation = it.trim() })
                    }
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = {
                            handleEvent(WeatherEvent.Save(weatherInfo.copy(geoLocation = nameLocation)))
                            onDismissRequest()
                        }
                    ) {
                        Text("Save")
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
                        Text("Cancel")
                    }
                }
            )
        } else {
            handleEvent(WeatherEvent.Save(weatherInfo))
        }
    }
}