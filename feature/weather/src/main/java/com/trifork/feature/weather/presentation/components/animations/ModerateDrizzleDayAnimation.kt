package com.trifork.feature.weather.presentation.components.animations

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.trifork.feature.weather.R
import com.trifork.feature.weather.presentation.components.animations.components.TranslateXAnimation
import com.trifork.feature.weather.presentation.components.animations.components.TranslateYAnimation

class ModerateDrizzleDayAnimation : AnimationDrawable {
    override fun build(): @Composable (BoxScope.() -> Unit) {
        return {
            Box(content = ClearDayAnimation(Modifier.size(50.dp)).build())
            TranslateYAnimation(
                modifier = Modifier.size(100.dp).offset(10.dp),
                animation = tween(3000, easing = FastOutSlowInEasing),
                iconRes = R.drawable.ic_rain_one
            )
            TranslateYAnimation(
                modifier = Modifier.size(100.dp).offset(20.dp),
                animation = tween(3000, easing = LinearOutSlowInEasing),
                iconRes = R.drawable.ic_rain_one
            )
            TranslateXAnimation(
                modifier = Modifier.size(100.dp),
                animation = tween(3000, easing = LinearEasing),
                iconRes = R.drawable.ic_cloud_two
            )
        }
    }
}