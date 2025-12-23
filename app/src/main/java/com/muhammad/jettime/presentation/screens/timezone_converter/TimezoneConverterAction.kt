package com.muhammad.jettime.presentation.screens.timezone_converter

import com.muhammad.jettime.domain.model.WorldTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed interface TimezoneConverterAction{
    data object GetWorldTimezones : TimezoneConverterAction
    data object ConvertTimezone : TimezoneConverterAction
    data object OnResetConversion: TimezoneConverterAction
    data class OnSelectToTimezone(val timezone : WorldTime) : TimezoneConverterAction
    data class OnSelectFromTimezone(val timezone : WorldTime) : TimezoneConverterAction
    data class OnSelectFromTime(val time : LocalTime) : TimezoneConverterAction
    data class OnSelectFromDate(val date : LocalDate) : TimezoneConverterAction
    data object OnToggleFromDatePicker : TimezoneConverterAction
    data object OnToggleFromTimePicker : TimezoneConverterAction
    data object OnToggleFromTimezonePicker : TimezoneConverterAction
    data object OnToggleToTimezonePicker : TimezoneConverterAction
}