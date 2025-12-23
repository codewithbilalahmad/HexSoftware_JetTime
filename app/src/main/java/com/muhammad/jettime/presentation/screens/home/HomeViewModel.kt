package com.muhammad.jettime.presentation.screens.home

import androidx.lifecycle.*
import com.muhammad.jettime.domain.repository.TimeZoneRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val timeZoneRepository: TimeZoneRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        onAction(HomeAction.GetTimezoneData)
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.GetTimezoneData -> getTimezoneData()
            is HomeAction.OnSearchQueryChange -> onSearchQueryChange(query = action.query)
        }
    }

    @OptIn(FlowPreview::class)
    private fun onSearchQueryChange(query: String) {
        viewModelScope.launch {
            _state.update { it.copy(searchQuery = query) }
            state.map { it.searchQuery }.debounce(300).distinctUntilChanged()
                .collectLatest { searchQuery ->
                    val filteredTimezones = _state.value.allWorldTimezones.filter {
                        it.city.contains(searchQuery, ignoreCase = true) || it.country.contains(
                            searchQuery,
                            ignoreCase = true
                        )
                    }
                    _state.update { it.copy(worldTimezones = filteredTimezones) }
                }
        }
    }

    private fun getTimezoneData() {
        viewModelScope.launch {
            while (true) {
                val worldTimezones = timeZoneRepository.getWorldTimeZones()
                _state.update {
                    it.copy(
                        worldTimezones = worldTimezones.drop(1),
                        allWorldTimezones = worldTimezones.drop(1),
                        currentTimezone = worldTimezones.first {timezone -> timezone.isCurrentTimezone }
                    )
                }
                delay(60_000)
            }
        }
    }
}