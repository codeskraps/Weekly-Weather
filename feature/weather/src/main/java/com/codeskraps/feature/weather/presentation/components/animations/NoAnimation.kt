package com.codeskraps.feature.weather.presentation.components.animations

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class NoAnimation(
    @DrawableRes val iconRes: Int
) : AnimationDrawable {
    override fun build(): @Composable (BoxScope.() -> Unit) {
        return {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}