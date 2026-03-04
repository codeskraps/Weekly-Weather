package com.codeskraps.feature.common.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private val SunOrange = Color(0xFFFFA500)

@Composable
fun SunLoadingIndicator(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "sunLoading")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 45f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sunRotation"
    )

    Canvas(modifier = modifier.size(64.dp)) {
        drawSun(rotation)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SunPullRefreshIndicator(
    refreshing: Boolean,
    state: PullRefreshState,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sunRefresh")
    val animatedRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 45f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sunRefreshRotation"
    )

    val progress = min(state.progress, 1f)
    val visible = refreshing || progress > 0.01f

    if (visible) {
        val verticalOffset = (80 * progress).dp

        Box(modifier = modifier, contentAlignment = Alignment.TopCenter) {
            Canvas(
                modifier = Modifier
                    .size(40.dp)
                    .offset(y = verticalOffset)
            ) {
                val rotation = if (refreshing) animatedRotation else progress * 360f
                val alpha = if (refreshing) 1f else (0.5f + 0.5f * progress)
                drawSun(rotation, alpha)
            }
        }
    }
}

private fun DrawScope.drawSun(rotation: Float, alpha: Float = 1f) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val radius = size.minDimension * 0.2f
    val rayInner = size.minDimension * 0.35f
    val rayOuter = size.minDimension * 0.47f
    val strokeWidth = size.minDimension * 0.08f
    val color = SunOrange.copy(alpha = alpha)

    rotate(degrees = rotation, pivot = center) {
        for (i in 0 until 8) {
            val angle = Math.toRadians((i * 45.0) - 90.0)
            val cos = cos(angle).toFloat()
            val sin = sin(angle).toFloat()
            drawLine(
                color = color,
                start = Offset(center.x + rayInner * cos, center.y + rayInner * sin),
                end = Offset(center.x + rayOuter * cos, center.y + rayOuter * sin),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }

        drawCircle(
            color = color,
            radius = radius,
            center = center
        )
    }
}
