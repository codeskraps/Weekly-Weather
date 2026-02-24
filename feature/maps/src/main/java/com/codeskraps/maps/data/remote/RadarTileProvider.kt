package com.codeskraps.maps.data.remote

import com.google.android.gms.maps.model.Tile
import com.google.android.gms.maps.model.TileProvider
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

class RadarTileProvider(private val urlTemplate: String) : TileProvider {

    companion object {
        private val cache = ConcurrentHashMap<String, ByteArray>()

        fun clearCache() {
            cache.clear()
        }
    }

    override fun getTile(x: Int, y: Int, zoom: Int): Tile? {
        if (zoom > 7) return TileProvider.NO_TILE

        val url = urlTemplate
            .replace("{z}", zoom.toString())
            .replace("{x}", x.toString())
            .replace("{y}", y.toString())

        cache[url]?.let { return Tile(256, 256, it) }

        return try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            try {
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val bytes = connection.inputStream.readBytes()
                    if (bytes.isNotEmpty()) {
                        cache[url] = bytes
                        Tile(256, 256, bytes)
                    } else {
                        null
                    }
                } else {
                    null
                }
            } finally {
                connection.disconnect()
            }
        } catch (e: Exception) {
            null // Return null so Google Maps retries with exponential backoff
        }
    }
}
