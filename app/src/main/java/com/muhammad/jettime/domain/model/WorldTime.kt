package com.muhammad.jettime.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class WorldTime(
    val zoneId: String,
    val city: String,
    val country: String,
    val time: String,
    val abbreviation: String,
    val utcOffset: String,
    val timeDifference: TimeDifference,
    val isDay : Boolean,
    val isCurrentTimezone : Boolean
)
