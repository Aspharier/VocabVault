package com.aspharier.vocabvault.presentation.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aspharier.vocabvault.ui.theme.DarkVocabColors
import com.aspharier.vocabvault.ui.theme.LightVocabColors
import com.aspharier.vocabvault.ui.theme.VocabColors

/**
 * App theme options: Dark ("Ink"), Light ("Paper"), System ("Follow system")
 */
enum class AppTheme(val displayName: String, val isDark: Boolean) {
    DARK("Dark", true),
    LIGHT("Light", false),
    SYSTEM("System", true);
}

enum class TextScale(val scale: Float, val label: String) {
    SMALL(0.92f, "S"),
    MEDIUM(1.0f, "M"),
    LARGE(1.11f, "L");

    companion object {
        fun fromString(value: String): TextScale = when (value.lowercase()) {
            "s", "small" -> SMALL
            "l", "large" -> LARGE
            else -> MEDIUM
        }
    }
}

val LocalVocabColors = staticCompositionLocalOf { DarkVocabColors }
val LocalTextScale = staticCompositionLocalOf { 1.0f }
val LocalAppTheme = staticCompositionLocalOf { AppTheme.DARK }

object VocabTheme {
    val colors: VocabColors
        @Composable
        @ReadOnlyComposable
        get() = LocalVocabColors.current

    val scale: Float
        @Composable
        @ReadOnlyComposable
        get() = LocalTextScale.current
}

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(2.dp),
    small = RoundedCornerShape(3.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(8.dp),
    extraLarge = RoundedCornerShape(12.dp)
)

private fun buildMaterialColorScheme(c: VocabColors): ColorScheme {
    return if (c.isDark) {
        darkColorScheme(
            primary = c.accent,
            onPrimary = c.accentInk,
            primaryContainer = c.accentWash,
            onPrimaryContainer = c.accent,
            secondary = c.accent,
            onSecondary = c.accentInk,
            background = c.bg,
            onBackground = c.ink,
            surface = c.raise,
            onSurface = c.ink,
            surfaceVariant = c.bgSunken,
            onSurfaceVariant = c.ink2,
            outline = c.rule2,
            outlineVariant = c.rule,
            error = c.danger,
            onError = c.accentInk
        )
    } else {
        lightColorScheme(
            primary = c.accent,
            onPrimary = c.accentInk,
            primaryContainer = c.accentWash,
            onPrimaryContainer = c.accent,
            secondary = c.accent,
            onSecondary = c.accentInk,
            background = c.bg,
            onBackground = c.ink,
            surface = c.raise,
            onSurface = c.ink,
            surfaceVariant = c.bgSunken,
            onSurfaceVariant = c.ink2,
            outline = c.rule2,
            outlineVariant = c.rule,
            error = c.danger,
            onError = c.accentInk
        )
    }
}

@Composable
fun VocabVaultTheme(
    appTheme: AppTheme = AppTheme.DARK,
    textScale: TextScale = TextScale.MEDIUM,
    content: @Composable () -> Unit
) {
    val systemInDark = isSystemInDarkTheme()
    val isDark = when (appTheme) {
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
        AppTheme.SYSTEM -> systemInDark
    }

    val vocabColors = if (isDark) DarkVocabColors else LightVocabColors
    val materialColors = buildMaterialColorScheme(vocabColors)

    CompositionLocalProvider(
        LocalVocabColors provides vocabColors,
        LocalTextScale provides textScale.scale,
        LocalAppTheme provides appTheme
    ) {
        MaterialTheme(
            colorScheme = materialColors,
            typography = AppTypography,
            shapes = AppShapes,
            content = content
        )
    }
}