package com.muhammad.jettime.presentation.screens.home

import com.muhammad.jettime.domain.model.WorldTime

data class HomeState(
    val currentTimezone : WorldTime?=null,
    val worldTimezones : List<WorldTime> = emptyList(),
    val allWorldTimezones : List<WorldTime> = emptyList(),
    val searchQuery : String = ""
)