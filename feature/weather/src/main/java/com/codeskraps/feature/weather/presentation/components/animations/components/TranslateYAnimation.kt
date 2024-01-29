package com.codeskraps.feature.weather.presentation.components.animations.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun TranslateYAnimation(
    modifier: Modifier,
    animation: TweenSpec<Float>,
    @DrawableRes iconRes: Int,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = animation,
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Image(
        painter = painterResource(iconRes),
        contentDescription = "",
        contentScale = ContentScale.FillWidth,
        modifier = modifier.graphicsLayer {
            translationY = progress
        }
    )
}