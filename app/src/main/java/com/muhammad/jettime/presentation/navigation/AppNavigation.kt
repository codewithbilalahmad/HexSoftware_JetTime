package com.muhammad.jettime.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.muhammad.jettime.presentation.screens.home.HomeScreen
import com.muhammad.jettime.presentation.screens.timezone_converter.TimezoneConverterScreen

@Composable
fun AppNavigation(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Destinations.HomeScreen) {
        composable<Destinations.HomeScreen> {
            HomeScreen(naHostController = navHostController)
        }
        composable<Destinations.TimezoneConverterScreen> {
            TimezoneConverterScreen(navHostController = navHostController)
        }
    }
}