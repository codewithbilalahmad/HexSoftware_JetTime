package com.muhammad.jettime.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class TimeDifference(
    val diffTime : String,
    val day : String
)