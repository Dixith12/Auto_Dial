package com.example.caller_dial.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.caller_dial.ui.screens.CallerScreen
import com.example.caller_dial.ui.screens.HomeScreen
import com.example.caller_dial.ui.screens.SummaryScreen

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {
        composable(NavRoutes.HOME) {
            HomeScreen(
                onStartCalling = { listId ->
                    navController.navigate("caller/$listId")
                }
            )
        }
        composable(
            route = NavRoutes.CALLER,
            arguments = listOf(
                navArgument("listId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->

            val listId =
                backStackEntry.arguments?.getLong("listId") ?: return@composable

            CallerScreen(
                listId = listId,
                onFinishCalling = {
                    navController.navigate(NavRoutes.SUMMARY) {
                        popUpTo(NavRoutes.HOME)
                    }
                }
            )
        }
        composable(NavRoutes.SUMMARY) {
            SummaryScreen(
                onBackToHome = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}
