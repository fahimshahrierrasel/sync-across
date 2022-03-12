package com.fahimshahrierrasel.syncacross.ui

sealed class NavigationScreen(val route: String) {}

sealed class NavigationScreens {
    object Login : NavigationScreen("login")
    object Home : NavigationScreen("home")
}