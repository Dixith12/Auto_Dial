package com.example.caller_dial.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.caller_dial.ui.screens.*

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {
        composable(NavRoutes.HOME) {
            HomeScreen()
        }
        composable(NavRoutes.CALLER) {
            CallerScreen()
        }
        composable(NavRoutes.SUMMARY) {
            SummaryScreen()
        }
    }
}
