package com.codeskraps.feature.weather.presentation.components.animations

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codeskraps.feature.weather.R
import com.codeskraps.feature.weather.presentation.components.animations.components.TranslateXAnimation
import com.codeskraps.feature.weather.presentation.components.animations.components.TranslateYAnimation

class ModerateThunderstormNightAnimation : AnimationDrawable {
    override fun build(): @Composable (BoxScope.() -> Unit) {
        return {
            Box(
                modifier = Modifier.padding(start = 50.dp),
                content = ClearNightAnimation(Modifier.size(50.dp)).build()
            )
            TranslateXAnimation(
                modifier = Modifier.size(100.dp),
                animation = tween(3000, easing = LinearEasing),
                iconRes = R.drawable.ic_cloud_two
            )
            TranslateYAnimation(
                modifier = Modifier
                    .size(100.dp)
                    .offset(10.dp),
                animation = tween(500, easing = LinearEasing),
                iconRes = R.drawable.ic_thunder_one
            )
        }
    }
}