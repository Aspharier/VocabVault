package com.aspharier.vocabvault.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aspharier.vocabvault.presentation.dictionary.DictionaryScreen
import com.aspharier.vocabvault.presentation.search.SearchScreen
import com.aspharier.vocabvault.presentation.settings.SettingsScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Search.route,
            modifier = Modifier.fillMaxSize(),
            enterTransition = { fadeIn(animationSpec = tween(220)) },
            exitTransition = { fadeOut(animationSpec = tween(220)) }
        ) {
            composable(BottomNavItem.Search.route) {
                SearchScreen()
            }
            composable(BottomNavItem.Vault.route) {
                DictionaryScreen()
            }
            composable(BottomNavItem.Settings.route) {
                SettingsScreen()
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            BottomNavBar(navController)
        }
    }
}
