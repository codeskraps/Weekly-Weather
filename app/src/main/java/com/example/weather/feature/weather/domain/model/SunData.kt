package com.example.weather.feature.weather.domain.model

import java.time.LocalDateTime

class SunData(
    val sunrise: List<LocalDateTime>,
    val sunset: List<LocalDateTime>
)