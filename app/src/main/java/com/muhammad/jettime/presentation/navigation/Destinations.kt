package com.muhammad.jettime.presentation.navigation

import kotlinx.serialization.*

interface Destinations {
    @Serializable
    data object HomeScreen : Destinations
    @Serializable
    data object TimezoneConverterScreen : Destinations
}