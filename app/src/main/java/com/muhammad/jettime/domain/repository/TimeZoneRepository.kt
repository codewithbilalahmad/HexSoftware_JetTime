package com.muhammad.jettime.domain.repository

import com.muhammad.jettime.domain.model.WorldTime

interface TimeZoneRepository {
    fun getWorldTimeZones() : List<WorldTime>
}