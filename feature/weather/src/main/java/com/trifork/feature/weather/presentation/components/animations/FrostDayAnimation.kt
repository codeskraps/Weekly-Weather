package com.trifork.feature.weather.presentation.components.animations

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.trifork.feature.weather.R

class FrostDayAnimation : AnimationDrawable {
    override fun build(): @Composable (BoxScope.() -> Unit) {
        return {
            Box(content = ClearDayAnimation(Modifier.size(50.dp)).build())
            Image(
                painter = painterResource(id = R.drawable.ic_frost),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}