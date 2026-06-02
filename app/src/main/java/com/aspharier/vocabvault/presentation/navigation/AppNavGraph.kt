package com.aspharier.vocabvault.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aspharier.vocabvault.presentation.dictionary.DictionaryScreen
import com.aspharier.vocabvault.presentation.home.HomeScreen
import com.aspharier.vocabvault.presentation.settings.SettingsScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen()
            }
            composable(BottomNavItem.Dictionary.route) {
                DictionaryScreen()
            }
            composable(BottomNavItem.Settings.route) {
                SettingsScreen()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            BottomNavBar(navController)
        }
    }
}
