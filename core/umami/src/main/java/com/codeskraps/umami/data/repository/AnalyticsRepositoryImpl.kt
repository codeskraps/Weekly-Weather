package com.codeskraps.umami.data.repository

import android.app.Application
import com.codeskraps.umami.data.remote.UmamiAnalyticsDataSource
import com.codeskraps.umami.data.remote.UmamiConfig
import com.codeskraps.umami.domain.AnalyticsRepository
import com.codeskraps.umami.domain.DeviceIdRepository

internal class AnalyticsRepositoryImpl(
    private val application: Application,
    private val deviceIdRepository: DeviceIdRepository
) : AnalyticsRepository {
    private val analyticsDataSource = UmamiAnalyticsDataSource(
        context = application,
        config = UmamiConfig(
            scriptUrl = "https://umami.codeskraps.com/script.js",
            websiteId = "047c76b3-6f42-45f0-ba9b-772b056ccdc1",
            baseUrl = "https://umami.codeskraps.com"
        )
    )

    override suspend fun initialize() {
        analyticsDataSource.initialize()
        // Identify the device as soon as analytics is initialized
        val deviceId = deviceIdRepository.getOrCreateDeviceId()
        identifyUser(deviceId)
    }

    override suspend fun trackPageView(pageName: String) {
        analyticsDataSource.trackPageView(pageName)
    }

    override suspend fun trackEvent(eventName: String, eventData: Map<String, String>) {
        analyticsDataSource.trackEvent(eventName, eventData)
    }

    override suspend fun identifyUser(walletAddress: String?) {
        analyticsDataSource.identifyUser(walletAddress)
    }
}