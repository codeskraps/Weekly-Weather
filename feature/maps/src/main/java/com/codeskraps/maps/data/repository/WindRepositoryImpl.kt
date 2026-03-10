package com.codeskraps.maps.data.repository

import android.util.Log
import com.codeskraps.feature.common.util.Resource
import com.codeskraps.maps.data.mappers.toWindData
import com.codeskraps.maps.data.remote.wind.OpenMeteoWindApi
import com.codeskraps.maps.domain.model.WindData
import com.codeskraps.maps.domain.repository.WindRepository

class WindRepositoryImpl(
    private val api: OpenMeteoWindApi
) : WindRepository {

    override suspend fun getWindGrid(
        latMin: Double,
        latMax: Double,
        lngMin: Double,
        lngMax: Double,
        gridSize: Int
    ): Resource<WindData> {
        return try {
            val latStep = (latMax - latMin) / (gridSize - 1).coerceAtLeast(1)
            val lngStep = (lngMax - lngMin) / (gridSize - 1).coerceAtLeast(1)

            val latitudes = mutableListOf<Double>()
            val longitudes = mutableListOf<Double>()

            for (row in 0 until gridSize) {
                for (col in 0 until gridSize) {
                    latitudes.add(latMin + row * latStep)
                    longitudes.add(lngMin + col * lngStep)
                }
            }

            val latStr = latitudes.joinToString(",") { "%.2f".format(it) }
            val lngStr = longitudes.joinToString(",") { "%.2f".format(it) }

            Log.d(TAG, "Fetching wind grid: ${latitudes.size} points")
            val response = api.getWindData(latitudes = latStr, longitudes = lngStr)
            Log.d(TAG, "API response: ${response.size} locations")
            Resource.Success(response.toWindData())
        } catch (e: Exception) {
            Log.e(TAG, "Wind API error", e)
            Resource.Error(e.message ?: "Failed to fetch wind data")
        }
    }

    companion object {
        private const val TAG = "WeatherApp:WindRepo"
    }
}
