package com.codeskraps.feature.weather.presentation.components.animations

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.codeskraps.feature.weather.R

class FrostNightAnimation : AnimationDrawable {
    override fun build(): @Composable (BoxScope.() -> Unit) {
        return {
            Box(
                modifier = Modifier.padding(start = 50.dp),
                content = ClearNightAnimation(Modifier.size(50.dp)).build()
            )
            Image(
                painter = painterResource(id = R.drawable.ic_frost),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .offset((-10).dp)
            )
        }
    }
}