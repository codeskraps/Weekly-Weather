package com.codeskraps.umami.domain

interface DeviceIdRepository {
    suspend fun getOrCreateDeviceId(): String
} 