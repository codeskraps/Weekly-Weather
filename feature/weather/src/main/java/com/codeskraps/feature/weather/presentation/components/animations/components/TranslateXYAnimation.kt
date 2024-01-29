package com.codeskraps.feature.weather.presentation.components.animations.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TweenSpec
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
fun TranslateXYAnimation(
    modifier: Modifier,
    animationX: TweenSpec<Float>,
    animationY: TweenSpec<Float>,
    @DrawableRes iconRes: Int,
) {

    val infiniteTransitionX = rememberInfiniteTransition(label = "")
    val progressX by infiniteTransitionX.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = animationX,
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val infiniteTransitionY = rememberInfiniteTransition(label = "")
    val progressY by infiniteTransitionY.animateFloat(
        initialValue = 0f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = animationY,
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Image(
        painter = painterResource(iconRes),
        contentDescription = "",
        contentScale = ContentScale.FillWidth,
        modifier = modifier.graphicsLayer {
            translationX = progressX
            translationY = progressY
        }
    )
}