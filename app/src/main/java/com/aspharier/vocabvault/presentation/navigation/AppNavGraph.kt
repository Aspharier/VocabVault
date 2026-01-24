package com.aspharier.vocabvault.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aspharier.vocabvault.presentation.home.HomeScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            // HomeScreen()
            HomeScreen()
        }
        composable("dictionary") {
            // DictionaryScreen()
        }
    }
}