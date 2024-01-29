package com.codeskraps.core.location.domain

import android.location.Location

interface LocationTracker {

    suspend fun getCurrentLocation(): Location?
}