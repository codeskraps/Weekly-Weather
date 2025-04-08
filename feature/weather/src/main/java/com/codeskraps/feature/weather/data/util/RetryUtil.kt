package com.codeskraps.feature.weather.data.util

import kotlinx.coroutines.delay
import kotlin.math.pow

suspend fun <T> retryWithExponentialBackoff(
    maxAttempts: Int = 3,
    initialDelayMs: Long = 1000,
    maxDelayMs: Long = 5000,
    factor: Double = 2.0,
    block: suspend () -> T
): T {
    var currentDelay = initialDelayMs
    repeat(maxAttempts - 1) { attempt ->
        try {
            return block()
        } catch (e: Exception) {
            // On last attempt, throw the exception
            if (attempt == maxAttempts - 2) throw e
            
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelayMs)
        }
    }
    return block() // Last attempt
} 