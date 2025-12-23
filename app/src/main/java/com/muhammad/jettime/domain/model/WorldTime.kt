package com.muhammad.jettime.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate

@Immutable
data class WorldTime(
    val zoneId: String,
    val city: String,
    val country: String,
    val time: String,
    val date : LocalDate,
    val abbreviation: String,
    val utcOffset: String,
    val timeDifference: TimeDifference,
    val isDay : Boolean,
    val isCurrentTimezone : Boolean
)
