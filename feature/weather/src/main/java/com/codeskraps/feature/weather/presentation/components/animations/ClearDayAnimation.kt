package com.codeskraps.feature.weather.presentation.components.animations

import androidx.compose.animation.core.RepeatMode
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codeskraps.feature.weather.domain.model.WeatherType
import com.codeskraps.feature.weather.presentation.components.animations.components.RotateAnimation

class ClearDayAnimation(
    private val modifier: Modifier = Modifier.size(100.dp)
) : AnimationDrawable {
    override fun build(): @Composable (BoxScope.() -> Unit) {
        return {
            RotateAnimation(
                modifier = modifier,
                iconRes = WeatherType.ClearSkyDay.iconRes,
                repeatMode = RepeatMode.Restart
            )
        }
    }
}