package com.muhammad.jettime.utils

import android.annotation.SuppressLint
import com.muhammad.jettime.domain.model.ClockFormat
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@SuppressLint("DefaultLocale")
fun LocalTime.toFormattedTime() : String{
    val hour24 = this.hour
    val hour = when{
        hour24 == 0 -> 12
        hour24 > 12 -> hour24 - 12
        else -> hour24
    }
    val minute = this.minute
    return String.format("%02d : %02d", hour, minute)
}

fun LocalTime.toClockFormat() : ClockFormat{
    return if(this.hour < 12) ClockFormat.AM else ClockFormat.PM
}

fun LocalDate.toFormattedDate() : String{
    val day = this.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
    val month = this.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
    val year =this.year
    return "$day, $month $year"
}