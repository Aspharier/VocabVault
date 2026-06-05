package com.aspharier.vocabvault.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem (
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = "home",
        label = "Home",
        icon = Icons.Default.Home
    )

    object Search : BottomNavItem(
        route = "search",
        label = "Search",
        icon = Icons.Default.Search
    )

    object Learn : BottomNavItem(
        route = "learn",
        label = "Learn",
        icon = Icons.Default.PlayArrow
    )

    object Dictionary : BottomNavItem(
        route = "dictionary",
        label = "Vault",
        icon = Icons.Default.Star
    )

    object Settings : BottomNavItem(
        route = "settings",
        label = "Settings",
        icon = Icons.Default.Settings
    )
}