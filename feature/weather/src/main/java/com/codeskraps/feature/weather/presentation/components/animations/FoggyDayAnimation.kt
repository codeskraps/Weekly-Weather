package com.codeskraps.feature.weather.presentation.components.animations

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codeskraps.feature.weather.R
import com.codeskraps.feature.weather.presentation.components.animations.components.TranslateXAnimation

class FoggyDayAnimation : AnimationDrawable {
    override fun build(): @Composable (BoxScope.() -> Unit) {
        return {
            Box(content = ClearDayAnimation(Modifier.size(50.dp)).build())
            TranslateXAnimation(
                modifier = Modifier.size(100.dp),
                animation = tween(3000, easing = FastOutSlowInEasing),
                iconRes = R.drawable.ic_foggy_one
            )
            TranslateXAnimation(
                modifier = Modifier.size(100.dp),
                animation = tween(4000, easing = LinearOutSlowInEasing),
                iconRes = R.drawable.ic_foggy_two
            )
            TranslateXAnimation(
                modifier = Modifier.size(100.dp),
                animation = tween(2000, easing = FastOutLinearInEasing),
                iconRes = R.drawable.ic_foggy_three
            )
            TranslateXAnimation(
                modifier = Modifier.size(100.dp),
                animation = tween(1000, easing = LinearEasing),
                iconRes = R.drawable.ic_foggy_four
            )
        }
    }
}