package com.codeskraps.umami.data.remote

import android.content.Context
import android.os.Build
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

internal data class UmamiConfig(
    val websiteId: String,
    val baseUrl: String
)

internal class UmamiAnalyticsDataSource(
    private val context: Context,
    private val config: UmamiConfig
) {
    private var isInitialized = false
    private val userAgent: String by lazy { buildUserAgent() }

    suspend fun initialize() {
        isInitialized = true
    }

    suspend fun trackPageView(pageName: String) {
        if (!isInitialized) return

        val url = if (pageName.startsWith("/")) pageName else "/$pageName"
        val title = pageName
            .replace("-", " ")
            .replaceFirstChar { it.uppercase() }

        val payload = JSONObject().apply {
            put("website", config.websiteId)
            put("url", url)
            put("title", title)
        }
        sendEvent("pageview", payload)
    }

    suspend fun trackEvent(eventName: String, eventData: Map<String, String> = emptyMap()) {
        if (!isInitialized) return

        val payload = JSONObject().apply {
            put("website", config.websiteId)
            put("name", eventName)
            if (eventData.isNotEmpty()) {
                put("data", JSONObject(eventData as Map<*, *>))
            }
        }
        sendEvent("event", payload)
    }

    suspend fun identifyUser(walletAddress: String?) {
        if (!isInitialized || walletAddress.isNullOrBlank()) return

        val addressLength = walletAddress.length
        val anonymizedId = if (addressLength > 8) {
            "${walletAddress.take(4)}...${walletAddress.takeLast(4)}"
        } else {
            walletAddress
        }

        val payload = JSONObject().apply {
            put("website", config.websiteId)
            put("data", JSONObject().apply {
                put("wallet_id", anonymizedId)
            })
        }
        sendEvent("identify", payload)
    }

    private suspend fun sendEvent(type: String, payload: JSONObject) = withContext(Dispatchers.IO) {
        try {
            val body = JSONObject().apply {
                put("type", type)
                put("payload", payload)
            }

            val connection = URL("${config.baseUrl}/api/send").openConnection() as HttpURLConnection
            connection.apply {
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("User-Agent", userAgent)
                connectTimeout = 5000
                readTimeout = 5000
                doOutput = true
            }

            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(body.toString())
                writer.flush()
            }

            val responseCode = connection.responseCode
            connection.disconnect()

            if (responseCode !in 200..299) {
                Log.w(TAG, "Umami API returned $responseCode for $type")
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to send $type event", e)
        }
    }

    private fun buildUserAgent(): String {
        val appVersion = try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: "unknown"
        } catch (_: Exception) {
            "unknown"
        }
        return "WeeklyWeather/$appVersion (Android ${Build.VERSION.RELEASE}; ${Build.MODEL}; ${Locale.getDefault().language})"
    }

    companion object {
        private const val TAG = "UmamiAnalytics"
    }
}
