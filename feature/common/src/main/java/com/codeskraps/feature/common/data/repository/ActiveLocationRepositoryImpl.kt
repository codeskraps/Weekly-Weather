package com.codeskraps.feature.common.data.repository

import com.codeskraps.feature.common.domain.model.ActiveLocation
import com.codeskraps.feature.common.domain.repository.ActiveLocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ActiveLocationRepositoryImpl : ActiveLocationRepository {
    private val _location = MutableStateFlow<ActiveLocation?>(null)
    override val location: StateFlow<ActiveLocation?> = _location.asStateFlow()

    override fun update(location: ActiveLocation) {
        _location.value = location
    }
}
