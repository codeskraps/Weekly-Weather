package com.trifork.feature.geocoding.presentation.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.NavController
import com.trifork.feature.common.components.ObserveAsEvents
import com.trifork.feature.common.navigation.Screen
import com.trifork.feature.common.R
import com.trifork.feature.geocoding.presentation.mvi.GeoAction
import com.trifork.feature.geocoding.presentation.mvi.GeoEvent
import com.trifork.feature.geocoding.presentation.mvi.GeoState
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun GeocodingScreen(
    navController: NavController,
    state: GeoState,
    handleEvent: (GeoEvent) -> Unit,
    action: Flow<GeoAction>
) {
    val context = LocalContext.current
    val resources = context.resources

    LifecycleResumeEffect {
        handleEvent(GeoEvent.LoadCache)
        onPauseOrDispose { }
    }

    ObserveAsEvents(flow = action) { onAction ->
        when (onAction) {
            is GeoAction.ShowToast -> {
                Toast.makeText(
                    context,
                    onAction.message,
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(resources.getString(R.string.search))
                },
                navigationIcon = {
                    Icon(
                        Icons.Default.ArrowBack,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = resources.getString(R.string.back),
                        modifier = Modifier.clickable { navController.navigateUp() },
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            var text by remember { mutableStateOf("") }
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current

            Column {

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(resources.getString(R.string.search_locations)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            handleEvent(GeoEvent.Search(text))
                        }
                    )
                )

                Box(modifier = Modifier.fillMaxSize()) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else if (state.error != null) {
                        Text(
                            text = state.error,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else if (state.geoLocations.isEmpty()) {
                        Text(
                            text = resources.getString(R.string.search_locations),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn {
                            itemsIndexed(state.geoLocations) { index, geoLocation ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable {
                                            navController.navigate(
                                                Screen.Weather.createRoute(
                                                    geoLocation.displayName(),
                                                    geoLocation.latitude,
                                                    geoLocation.longitude
                                                )
                                            ) {
                                                popUpTo(navController.graph.id) {
                                                    inclusive = true
                                                }
                                            }
                                        },
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = geoLocation.displayName(),
                                        modifier = Modifier
                                            .align(CenterVertically)
                                            .padding(start = 16.dp, top = 15.dp, bottom = 15.dp)
                                            .fillMaxHeight()
                                    )
                                    Icon(
                                        imageVector = if (geoLocation.cached) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        tint = if (geoLocation.cached) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.outline,
                                        contentDescription = resources.getString(R.string.cached),
                                        modifier = Modifier
                                            .align(CenterVertically)
                                            .padding(end = 16.dp)
                                            .clickable {
                                                handleEvent(
                                                    if (geoLocation.cached) {
                                                        GeoEvent.Delete(geoLocation)
                                                    } else {
                                                        GeoEvent.Save(geoLocation)
                                                    }
                                                )
                                            }
                                    )
                                }
                                if (index < state.geoLocations.lastIndex)
                                    Divider(color = Color.Black, thickness = 1.dp)
                            }
                        }
                    }
                }
            }
        }
    }
}