package com.codeskraps.feature.weather.presentation.components.animations.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun RotateAnimation(
    modifier: Modifier,
    @DrawableRes iconRes: Int,
    repeatMode: RepeatMode
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 45f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = repeatMode
        ), label = ""
    )

    Image(
        painter = painterResource(iconRes),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = modifier.graphicsLayer {
            rotationZ = progress
        }
    )
}