package com.codeskraps.feature.common.domain.repository

import com.codeskraps.feature.common.domain.model.ActiveLocation
import kotlinx.coroutines.flow.StateFlow

interface ActiveLocationRepository {
    val location: StateFlow<ActiveLocation?>
    fun update(location: ActiveLocation)
}
