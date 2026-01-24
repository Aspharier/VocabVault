package com.aspharier.vocabvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import com.aspharier.vocabvault.presentation.navigation.AppNavGraph
import com.aspharier.vocabvault.presentation.theme.VocabVaultTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            VocabVaultTheme(
                darkTheme = isSystemInDarkTheme()
            ) {
                AppNavGraph()
            }
        }
    }
}