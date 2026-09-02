package com.aspharier.vocabvault.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Search : BottomNavItem(
        route = "search",
        label = "Search",
        icon = Icons.Default.Search
    )

    object Vault : BottomNavItem(
        route = "vault",
        label = "Vault",
        icon = Icons.Default.Star
    )

    object Settings : BottomNavItem(
        route = "settings",
        label = "Settings",
        icon = Icons.Default.Settings
    )
}