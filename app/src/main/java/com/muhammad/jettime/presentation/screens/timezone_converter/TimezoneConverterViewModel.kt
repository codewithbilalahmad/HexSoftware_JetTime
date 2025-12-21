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

class TimezoneConverterViewModel(
    private val timeZoneRepository: TimeZoneRepository
) : ViewModel(){
    private val _state = MutableStateFlow(TimezoneConverterState())
    val state = _state.asStateFlow()
    init {
        onAction(TimezoneConverterAction.GetWorldTimezones)
    }
    fun onAction(action: TimezoneConverterAction){
        when(action){
            TimezoneConverterAction.ConvertTimezones -> convertTimezones()
            TimezoneConverterAction.GetWorldTimezones -> getWorldTimezones()
            is TimezoneConverterAction.OnSelectFromDate -> onSelectFromDate(action.time)
            is TimezoneConverterAction.OnSelectFromTime -> onSelectFromTime(action.time)
            is TimezoneConverterAction.OnSelectFromTimezone -> onSelectFromTimezone(action.timezone)
            is TimezoneConverterAction.OnSelectToTimezone -> onSelectToTimezone(action.timezone)
        }
    }

    private fun onSelectToTimezone(timezone: WorldTime) {
        _state.update { it.copy(selectedToTimezone = timezone) }
    }

    private fun convertTimezones() {
    }

    private fun onSelectFromTime(time: LocalTime) {
        _state.update { it.copy(selectedFromTime = time) }
    }

    private fun onSelectFromTimezone(timezone: WorldTime) {
        _state.update { it.copy(selectedFromTimezone = timezone) }
    }

    private fun onSelectFromDate(date: LocalDate) {
        _state.update { it.copy(selectedFromDate = date) }
    }

    private fun getWorldTimezones() {
        viewModelScope.launch {
            while(true){
                val worldTimezones = timeZoneRepository.getWorldTimeZones()
                _state.update { it.copy(worldTimezones = worldTimezones) }
                delay(60_000L)
            }
        }
    }
}