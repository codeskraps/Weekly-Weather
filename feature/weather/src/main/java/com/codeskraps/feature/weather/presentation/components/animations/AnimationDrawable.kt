package com.codeskraps.feature.weather.presentation.components.animations

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable

interface AnimationDrawable {

    fun build(): @Composable (BoxScope.() -> Unit)
}