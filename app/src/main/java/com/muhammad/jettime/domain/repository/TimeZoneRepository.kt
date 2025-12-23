package com.muhammad.jettime.domain.repository

import com.muhammad.jettime.domain.model.WorldTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

interface TimeZoneRepository {
    fun getWorldTimeZones() : List<WorldTime>
    fun convertTimezone(
        fromZoneId : String,
        toZoneId : String,
        date : LocalDate,
        time : LocalTime
    ) : WorldTime
}