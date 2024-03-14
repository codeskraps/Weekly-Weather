package com.codeskraps.feature.geocoding.presentation.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.codeskraps.feature.common.R
import com.codeskraps.feature.common.components.ObserveAsEvents
import com.codeskraps.feature.common.navigation.Screen
import com.codeskraps.feature.geocoding.presentation.mvi.GeoAction
import com.codeskraps.feature.geocoding.presentation.mvi.GeoEvent
import com.codeskraps.feature.geocoding.presentation.mvi.GeoState
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun GeocodingScreen(
    state: GeoState,
    handleEvent: (GeoEvent) -> Unit,
    action: Flow<GeoAction>,
    navUp: () -> Unit,
    navRoute: (String) -> Unit
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
                Toast.makeText(context, onAction.message, Toast.LENGTH_SHORT).show()
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
                    IconButton(onClick = { navUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = resources.getString(R.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navRoute(Screen.Map.route) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_map),
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "TODO"
                        )
                    }
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
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current

            Column {

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = state.query,
                    onValueChange = { handleEvent(GeoEvent.Search(it)) },
                    label = { Text(resources.getString(R.string.search_locations)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            //handleEvent(GeoEvent.Search(state.query))
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
                                            navRoute(
                                                Screen.Weather.createRoute(
                                                    geoLocation.displayName(),
                                                    geoLocation.latitude,
                                                    geoLocation.longitude
                                                )
                                            )
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
                                    Spacer(
                                        modifier = Modifier
                                            .background(color = Color.Black)
                                            .height(1.dp)
                                    )
                            }
                        }
                    }
                }
            }
        }
    }
}