package com.muhammad.jettime.presentation.screens.timezone_converter

import com.muhammad.jettime.domain.model.WorldTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
data class TimezoneConverterState(
    val worldTimezones: List<WorldTime> = emptyList(),
    val conversionResult: WorldTime? = null,
    val selectedFromDate: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    val selectedFromTime: LocalTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).time,
    val selectedFromTimezone: WorldTime? = null,
    val selectedToTimezone: WorldTime? = null
)
