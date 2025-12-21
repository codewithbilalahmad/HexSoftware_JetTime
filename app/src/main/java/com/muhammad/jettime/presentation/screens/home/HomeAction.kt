package com.muhammad.jettime.presentation.screens.home

sealed interface HomeAction{
    data object GetTimezoneData : HomeAction
    data class OnSearchQueryChange(val query : String) : HomeAction
}