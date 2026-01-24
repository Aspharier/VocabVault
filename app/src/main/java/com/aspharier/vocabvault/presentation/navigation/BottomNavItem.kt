package com.aspharier.vocabvault.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem (
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = "home",
        label = "Search",
        icon = Icons.Default.Search
    )

    object Dictionary : BottomNavItem(
        route = "dictionary",
        label = "Dictionary",
        icon = Icons.Default.Star
    )
}