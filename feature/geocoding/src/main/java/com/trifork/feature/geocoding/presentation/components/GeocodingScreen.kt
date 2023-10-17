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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.trifork.feature.common.navigation.Screen
import com.trifork.feature.geocoding.presentation.GeocodingViewModel
import com.trifork.feature.geocoding.presentation.mvi.GeoAction
import com.trifork.feature.geocoding.presentation.mvi.GeoEvent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun GeocodingScreen(
    navController: NavController,
    viewModel: GeocodingViewModel
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    LifecycleResumeEffect {
        viewModel.state.handleEvent(GeoEvent.LoadCache)
        onPauseOrDispose { }
    }

    LaunchedEffect(viewModel.action) {
        viewModel
            .action
            .collect { geoAction ->
                when (geoAction) {
                    is GeoAction.ShowToast -> {
                        Toast.makeText(
                            context,
                            geoAction.message,
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
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
                    Text("Search")
                },
                navigationIcon = {
                    Icon(
                        Icons.Default.ArrowBack,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "Back",
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
                    label = { Text("Search Locations") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            viewModel.state.handleEvent(GeoEvent.Search(text))
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
                            text = state.error ?: "",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else if (state.geoLocations.isEmpty()) {
                        Text(
                            text = "Search Locations",
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
                                                    "${geoLocation.name}, ${geoLocation.country}",
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
                                        text = "${geoLocation.name}, ${geoLocation.country}",
                                        modifier = Modifier
                                            .align(CenterVertically)
                                            .padding(start = 16.dp, top = 15.dp, bottom = 15.dp)
                                            .fillMaxHeight()
                                    )
                                    Icon(
                                        imageVector = if (geoLocation.cached) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        tint = if (geoLocation.cached) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.outline,
                                        contentDescription = "Cached",
                                        modifier = Modifier
                                            .align(CenterVertically)
                                            .padding(end = 16.dp)
                                            .clickable {
                                                viewModel.state.handleEvent(
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