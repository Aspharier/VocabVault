package com.aspharier.vocabvault

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import com.aspharier.vocabvault.presentation.navigation.AppNavGraph
import com.aspharier.vocabvault.presentation.theme.VocabVaultTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VocabVaultTheme(
                darkTheme = isSystemInDarkTheme()
            ) {
                AppNavGraph()
            }
        }
    }
}