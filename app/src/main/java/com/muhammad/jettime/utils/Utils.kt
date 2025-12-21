package com.muhammad.jettime.utils

import android.annotation.SuppressLint
import com.muhammad.jettime.domain.model.ClockFormat
import kotlinx.datetime.LocalTime

@SuppressLint("DefaultLocale")
fun LocalTime.toFormattedTime() : String{
    val hour = this.hour
    val minute = this.minute
    return String.format("%02d:%02d", hour, minute)
}

fun LocalTime.toClockFormat() : ClockFormat{
    return if(this.hour < 12) ClockFormat.AM else ClockFormat.PM
}