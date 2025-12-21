package com.muhammad.jettime.data

import android.annotation.SuppressLint
import com.muhammad.jettime.domain.model.TimeDifference
import com.muhammad.jettime.domain.model.WorldTime
import com.muhammad.jettime.domain.repository.TimeZoneRepository
import kotlinx.datetime.TimeZone
import kotlinx.datetime.offsetAt
import kotlinx.datetime.toLocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.abs
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class TimeZoneRepositoryImp : TimeZoneRepository {
    override fun getWorldTimeZones(): List<WorldTime> {
        val currentTimeZone = getCurrentZoneId()
        val timeZones = TimeZone.availableZoneIds.map { zoneId ->
            val (city, country) = parseCityAndCountry(zoneId)
            WorldTime(
                zoneId = zoneId,
                city = city,
                country = country, timeDifference = getTimeDifference(zoneId),
                abbreviation = getAbbreviation(zoneId),
                time = getFormattedTime(zoneId), isDay = isDayTime(zoneId),
                isCurrentTimezone = currentTimeZone == zoneId, utcOffset = getUtcOffset(zoneId)
            )
        }
        return timeZones.sortedWith(
            compareByDescending<WorldTime> { it.isCurrentTimezone }.thenBy {
                it.country
            }
        )
    }

    fun parseCityAndCountry(zoneId: String): Pair<String, String> {
        val parts = zoneId.split("/")
        val country = parts.first().replace("_", " ")
        val city = parts.last().replace("_", " ")
        return city to country
    }

    @SuppressLint("DefaultLocale")
    fun getFormattedTime(zoneId: String): String {
        val zone = TimeZone.of(zoneId)
        val instant = Clock.System.now()
        val localDateTime = instant.toLocalDateTime(zone)
        val hour24 = localDateTime.hour
        val minute = localDateTime.minute
        val hour12 = when {
            hour24 == 0 -> 12
            hour24 > 12 -> hour24 - 12
            else -> hour24
        }
        val amPm = if (hour24 < 12) "AM" else "PM"
        return String.format("%02d:%02d %s", hour12, minute, amPm)
    }

    fun getCurrentZoneId(): String {
        return TimeZone.currentSystemDefault().id
    }

    fun getUtcOffset(zoneId: String): String {
        val zone = TimeZone.of(zoneId)
        val instant = Clock.System.now()
        val offsetSeconds = zone.offsetAt(instant).totalSeconds
        val hours = offsetSeconds / 3600
        val minutes = (abs(offsetSeconds) % 3600) / 60
        return if (minutes == 0) {
            if (hours > 0) "+$hours" else hours.toString()
        } else {
            val sign = if (offsetSeconds > 0) "+" else "-"
            "$sign${abs(hours)}.${minutes * 10 / 60}"
        }
    }

    @SuppressLint("NewApi")
    fun getAbbreviation(zoneId: String): String {
        return ZonedDateTime.now(ZoneId.of(zoneId)).zone.getDisplayName(
            TextStyle.SHORT,
            Locale.getDefault()
        )
    }

    fun isDayTime(zoneId: String): Boolean {
        val zone = TimeZone.of(zoneId)
        val instant = Clock.System.now()
        val localDateTime = instant.toLocalDateTime(zone)
        val hour = localDateTime.hour
        return hour in 6..17
    }

    fun getTimeDifference(zoneId: String): TimeDifference {
        val nowInstant = Clock.System.now()
        val systemZone = TimeZone.currentSystemDefault()
        val targetZone = TimeZone.of(zoneId)
        val systemDateTime = nowInstant.toLocalDateTime(systemZone)
        val targetZonedDateTime = nowInstant.toLocalDateTime(targetZone)
        val systemMinutes = systemDateTime.hour * 60 + systemDateTime.minute
        val targetZoneMinutes = targetZonedDateTime.hour * 60 + targetZonedDateTime.minute
        val minuteDifference = targetZoneMinutes - systemMinutes
        val absMinutes = abs(minuteDifference)
        val hours = absMinutes / 60
        val minutes = absMinutes % 60
        val sign = if (minuteDifference > 0) "+" else "-"
        val signText = if (sign == "+") "ahead" else "behind"
        val timeDifference = buildString {
            append(sign)
            if (hours > 0) append("${hours}h ")
            if (minutes > 0) append("${minutes}m ")
            append(signText)
        }.trim()
        val dayDiff =
            (targetZonedDateTime.date.toEpochDays() - systemDateTime.date.toEpochDays()).toInt()
        val dayDifference = when (dayDiff) {
            -1 -> "Yesterday"
            0 -> "Today"
            1 -> "Tomorrow"
            else -> if (dayDiff > 0) "+$dayDiff days" else "$dayDiff days"
        }
        return TimeDifference(diffTime = timeDifference, day = dayDifference)
    }
}