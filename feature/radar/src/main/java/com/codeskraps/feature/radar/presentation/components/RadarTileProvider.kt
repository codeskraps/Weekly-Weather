package com.codeskraps.feature.radar.presentation.components

import com.google.android.gms.maps.model.UrlTileProvider
import java.net.URL

class RadarTileProvider(private val urlTemplate: String) : UrlTileProvider(256, 256) {
    override fun getTileUrl(x: Int, y: Int, zoom: Int): URL? {
        if (zoom > 7) return null
        val url = urlTemplate
            .replace("{z}", zoom.toString())
            .replace("{x}", x.toString())
            .replace("{y}", y.toString())
        return URL(url)
    }
}
