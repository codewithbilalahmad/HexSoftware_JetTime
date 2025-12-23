package com.muhammad.jettime.data

import android.annotation.SuppressLint
import com.muhammad.jettime.domain.model.TimeDifference
import com.muhammad.jettime.domain.model.WorldTime
import com.muhammad.jettime.domain.repository.TimeZoneRepository
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.offsetAt
import kotlinx.datetime.toInstant
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
                city = city, date = observeDate(zoneId),
                country = country, timeDifference = getTimeDifference(zoneId),
                abbreviation = getAbbreviation(zoneId),
                time = getFormattedTime(zoneId), isDay = isDayTime(zoneId),
                isCurrentTimezone = currentTimeZone == zoneId, utcOffset = getUtcOffset(zoneId)
            )
        }
        return timeZones.sortedWith(
            compareByDescending<WorldTime> { it.isCurrentTimezone }.thenBy {
                it.city
            }
        )
    }

    override fun convertTimezone(
        fromZoneId: String,
        toZoneId: String,
        date: LocalDate,
        time: LocalTime,
    ): WorldTime {
        val currentTimeZone = getCurrentZoneId()
        val fromZone = TimeZone.of(fromZoneId)
        val toZone = TimeZone.of(toZoneId)
        val fromDateTime = LocalDateTime(date, time)
        val instant = fromDateTime.toInstant(fromZone)
        val toDateTime = instant.toLocalDateTime(toZone)
        val isDay = toDateTime.hour in 6..17
        val (city, country) = parseCityAndCountry(toZoneId)
        return WorldTime(
            zoneId = toZoneId,
            city = city,
            country = country,
            time = formatLocalTime(toDateTime.time),
            abbreviation = getAbbreviation(toZoneId),
            isDay = isDay, date = toDateTime.date,
            timeDifference = getTimeDifference(toZoneId),
            isCurrentTimezone = toZoneId == currentTimeZone,
            utcOffset = getUtcOffset(toZoneId)
        )
    }

    fun observeDate(zoneId: String): LocalDate {
        val timezone = TimeZone.of(zoneId)
        val instant = Clock.System.now()
        return instant.toLocalDateTime(timezone).date
    }

    @SuppressLint("DefaultLocale")
    fun formatLocalTime(time: LocalTime): String {
        val hour24 = time.hour
        val hour12 = when {
            hour24 == 0 -> 12
            hour24 > 12 -> hour24 - 12
            else -> hour24
        }
        val minute = time.minute
        val amPm = if (hour24 < 12) "AM" else "PM"
        return String.format("%02d:%02d %s", hour12, minute, amPm)
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
        return String.format("%02d : %02d %s", hour12, minute, amPm)
    }

    fun getCurrentZoneId(): String {
        return TimeZone.currentSystemDefault().id
    }

    @SuppressLint("DefaultLocale")
    fun getUtcOffset(zoneId: String): String {
        val zone = TimeZone.of(zoneId)
        val instant = Clock.System.now()
        val offsetSeconds = zone.offsetAt(instant).totalSeconds
        val hours = offsetSeconds / 3600
        val minutes = abs(offsetSeconds % 3600) / 60
        val sign = if (offsetSeconds > 0) "+" else "-"
        return if (minutes == 0) {
            "$sign${abs(hours)}"
        } else {
            String.format("%s%02d:%02d", sign, abs(hours), minutes)
        }
    }

    @SuppressLint("NewApi")
    fun getAbbreviation(zoneId: String): String {
        val zone = ZoneId.of(zoneId)
        val name = ZonedDateTime.now(zone).zone.getDisplayName(
            TextStyle.SHORT,
            Locale.getDefault()
        )
        return if (name.startsWith("GMT")) {
            zone.id.substringAfterLast("/").take(3).uppercase()
        } else name
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