package com.aspharier.vocabvault

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.aspharier.vocabvault.presentation.navigation.AppNavGraph
import com.aspharier.vocabvault.presentation.theme.AppTheme
import com.aspharier.vocabvault.presentation.theme.ThemeViewModel
import com.aspharier.vocabvault.presentation.theme.VocabVaultTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val currentTheme by themeViewModel.currentTheme.collectAsState()
            val textScale by themeViewModel.textScale.collectAsState()

            VocabVaultTheme(appTheme = currentTheme, textScale = textScale) {
                // Dynamically update system bars to match theme
                val isDark = when (currentTheme) {
                    AppTheme.DARK -> true
                    AppTheme.LIGHT -> false
                    AppTheme.SYSTEM -> androidx.compose.foundation.isSystemInDarkTheme()
                }
                SystemBarStyleEffect(isDarkTheme = isDark)

                // Fill the entire window with theme background
                // This eliminates white bleed behind bottom nav
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    AppNavGraph()
                }
            }
        }
    }
}

/**
 * Side effect that updates system bar (status bar + navigation bar)
 * styles whenever the theme's dark/light mode changes.
 * Makes status bar icons light on dark themes and dark on light themes,
 * and sets both bars to fully transparent.
 */
@Composable
private fun SystemBarStyleEffect(isDarkTheme: Boolean) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        DisposableEffect(isDarkTheme) {
            val activity = view.context as ComponentActivity
            val window = activity.window

            // Make both system bars fully transparent
            activity.enableEdgeToEdge(
                statusBarStyle = if (isDarkTheme) {
                    SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                } else {
                    SystemBarStyle.light(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT
                    )
                },
                navigationBarStyle = if (isDarkTheme) {
                    SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                } else {
                    SystemBarStyle.light(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT
                    )
                }
            )

            // Set status bar icon colors (light icons for dark theme, dark icons for light)
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !isDarkTheme
                isAppearanceLightNavigationBars = !isDarkTheme
            }

            onDispose {}
        }
    }
}