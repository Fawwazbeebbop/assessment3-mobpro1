package org.d3if3051.assessment3.navigation

sealed class ScreenNav(val route: String) {
    data object Welcome :ScreenNav("welcomeScreen")
    data object App : ScreenNav("appScreen")
}