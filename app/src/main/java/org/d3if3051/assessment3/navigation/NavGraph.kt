package org.d3if3051.assessment3.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.d3if3051.assessment3.screen.AppScreen
import org.d3if3051.assessment3.screen.WelcomeScreen

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()){
    NavHost(navController = navController, startDestination = ScreenNav.Welcome.route){
        composable(route = ScreenNav.Welcome.route){
            WelcomeScreen(navController)
        }
        composable(route = ScreenNav.App.route){
            AppScreen(navController)
        }

    }
}