package com.codeskraps.umami.data.repository

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import com.codeskraps.umami.domain.DeviceIdRepository
import java.util.UUID

class DeviceIdRepositoryImpl(
    private val application: Application
) : DeviceIdRepository {

    private val prefs: SharedPreferences = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override suspend fun getOrCreateDeviceId(): String {
        return prefs.getString(KEY_DEVICE_ID, null) ?: generateAndSaveDeviceId()
    }

    private fun generateAndSaveDeviceId(): String {
        val deviceId = UUID.randomUUID().toString()
        prefs.edit().putString(KEY_DEVICE_ID, deviceId).apply()
        return deviceId
    }

    companion object {
        private const val PREFS_NAME = "weather_device_prefs"
        private const val KEY_DEVICE_ID = "device_id"
    }
} 