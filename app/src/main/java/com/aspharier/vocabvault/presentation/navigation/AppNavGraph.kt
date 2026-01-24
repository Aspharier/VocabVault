package com.aspharier.vocabvault.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aspharier.vocabvault.presentation.dictionary.DictionaryScreen
import com.aspharier.vocabvault.presentation.home.HomeScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    Scaffold(
       bottomBar = {
           BottomNavBar(navController)
       }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(BottomNavItem.Home.route) {
                // HomeScreen()
                HomeScreen()
            }
            composable(BottomNavItem.Dictionary.route) {
                // DictionaryScreen()
                DictionaryScreen()
            }
        }
    }
}