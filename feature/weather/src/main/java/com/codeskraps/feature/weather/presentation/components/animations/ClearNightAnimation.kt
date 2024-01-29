package com.codeskraps.feature.weather.presentation.components.animations

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.codeskraps.feature.weather.R
import com.codeskraps.feature.weather.presentation.components.animations.components.AlphaAnimation

class ClearNightAnimation(
    private val modifier: Modifier = Modifier.size(100.dp)
) : AnimationDrawable {
    override fun build(): @Composable (BoxScope.() -> Unit) {
        return {
            AlphaAnimation(
                modifier = modifier,
                initValue = 1f,
                targetValue = 0.25f,
                animation = tween(1000, easing = FastOutSlowInEasing),
                iconRes = R.drawable.ic_star_one,
            )
            AlphaAnimation(
                modifier = modifier,
                initValue = 1f,
                targetValue = 0.25f,
                animation = tween(2000, easing = LinearOutSlowInEasing),
                iconRes = R.drawable.ic_star_two,
            )
            Image(
                painter = painterResource(id = R.drawable.ic_moon),
                contentDescription = null,
                modifier = modifier
            )
        }
    }
}