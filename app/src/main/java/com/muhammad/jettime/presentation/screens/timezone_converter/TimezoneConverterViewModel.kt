package com.muhammad.jettime.presentation.screens.timezone_converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammad.jettime.domain.model.WorldTime
import com.muhammad.jettime.domain.repository.TimeZoneRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class TimezoneConverterViewModel(
    private val timeZoneRepository: TimeZoneRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(TimezoneConverterState())
    val state = _state.asStateFlow()

    init {
        onAction(TimezoneConverterAction.GetWorldTimezones)
    }

    fun onAction(action: TimezoneConverterAction) {
        when (action) {
            TimezoneConverterAction.ConvertTimezone -> convertTimezone()
            TimezoneConverterAction.GetWorldTimezones -> getWorldTimezones()
            is TimezoneConverterAction.OnSelectFromDate -> onSelectFromDate(action.date)
            is TimezoneConverterAction.OnSelectFromTime -> onSelectFromTime(action.time)
            is TimezoneConverterAction.OnSelectFromTimezone -> onSelectFromTimezone(action.timezone)
            is TimezoneConverterAction.OnSelectToTimezone -> onSelectToTimezone(action.timezone)
            TimezoneConverterAction.OnToggleFromDatePicker -> {
                _state.update { it.copy(showFromDatePicker = !it.showFromDatePicker) }
            }

            TimezoneConverterAction.OnToggleFromTimePicker -> {
                _state.update { it.copy(showFromTimePicker = !it.showFromTimePicker) }
            }

            TimezoneConverterAction.OnToggleFromTimezonePicker -> {
                _state.update { it.copy(showFromTimezonePicker = !it.showFromTimezonePicker) }
            }

            TimezoneConverterAction.OnToggleToTimezonePicker -> {
                _state.update { it.copy(showToTimezonePicker = !it.showToTimezonePicker) }
            }

            TimezoneConverterAction.OnResetConversion -> onResetConversion()
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun onResetConversion() {
        _state.update {
            it.copy(
                selectedFromTimezone = null,
                selectedToTimezone = null,
                conversionResult = null, selectedFromDate = Clock.System.now().toLocalDateTime(
                    TimeZone.currentSystemDefault()
                ).date, selectedFromTime = Clock.System.now().toLocalDateTime(
                    TimeZone.currentSystemDefault()
                ).time
            )
        }
    }

    private fun onSelectToTimezone(timezone: WorldTime) {
        _state.update { it.copy(showToTimezonePicker = false, selectedToTimezone = timezone) }
    }

    private fun convertTimezone() {
        val fromTimezoneId = state.value.selectedFromTimezone?.zoneId ?: return
        val toTimezoneId = state.value.selectedToTimezone?.zoneId ?: return
        val date = state.value.selectedFromDate
        val time = state.value.selectedFromTime
        val conversionResult = timeZoneRepository.convertTimezone(
            fromZoneId = fromTimezoneId,
            toZoneId = toTimezoneId,
            date = date,
            time = time
        )
        _state.update { it.copy(conversionResult = conversionResult) }
    }

    private fun onSelectFromTime(time: LocalTime) {
        _state.update { it.copy(showFromTimePicker = false, selectedFromTime = time) }
    }

    private fun onSelectFromTimezone(timezone: WorldTime) {
        _state.update { it.copy(showFromTimezonePicker = false, selectedFromTimezone = timezone) }
    }

    private fun onSelectFromDate(date: LocalDate) {
        _state.update { it.copy(showFromDatePicker = false, selectedFromDate = date) }
    }

    @OptIn(ExperimentalTime::class)
    private fun getWorldTimezones() {
        viewModelScope.launch {
            var lastMinute = -1
            while (true) {
                val currentMinute = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).minute
                if (currentMinute != lastMinute) {
                    val worldTimezones = timeZoneRepository.getWorldTimeZones()
                    _state.update { it.copy(worldTimezones = worldTimezones) }
                    lastMinute = currentMinute
                }
                delay(1000L)
            }
        }
    }
}