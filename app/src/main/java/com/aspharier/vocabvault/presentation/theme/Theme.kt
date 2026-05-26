package com.aspharier.vocabvault.presentation.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aspharier.vocabvault.ui.theme.*

/**
 * All available app themes.
 */
enum class AppTheme(val displayName: String, val isDark: Boolean) {
    OBSIDIAN_DARK("Obsidian Dark", true),
    ARCTIC_FROST("Arctic Frost", false),
    CYBER_NEON("Cyber Neon", true),
    OCEAN_DEPTH("Ocean Depth", true),
    SAKURA_BLOOM("Sakura Bloom", false);
}

val LocalAppTheme = staticCompositionLocalOf { AppTheme.OBSIDIAN_DARK }

// ── Color Schemes ──────────────────────────────────

private val ObsidianColorScheme = darkColorScheme(
    primary = obsidian_primary,
    onPrimary = obsidian_onPrimary,
    primaryContainer = obsidian_primaryContainer,
    onPrimaryContainer = obsidian_onPrimaryContainer,
    secondary = obsidian_secondary,
    onSecondary = obsidian_onSecondary,
    secondaryContainer = obsidian_secondaryContainer,
    onSecondaryContainer = obsidian_onSecondaryContainer,
    tertiary = obsidian_tertiary,
    onTertiary = obsidian_onTertiary,
    tertiaryContainer = obsidian_tertiaryContainer,
    onTertiaryContainer = obsidian_onTertiaryContainer,
    error = obsidian_error,
    onError = obsidian_onError,
    errorContainer = obsidian_errorContainer,
    onErrorContainer = obsidian_onErrorContainer,
    background = obsidian_background,
    onBackground = obsidian_onBackground,
    surface = obsidian_surface,
    onSurface = obsidian_onSurface,
    surfaceVariant = obsidian_surfaceVariant,
    onSurfaceVariant = obsidian_onSurfaceVariant,
    outline = obsidian_outline,
    outlineVariant = obsidian_outlineVariant,
)

private val ArcticColorScheme = lightColorScheme(
    primary = arctic_primary,
    onPrimary = arctic_onPrimary,
    primaryContainer = arctic_primaryContainer,
    onPrimaryContainer = arctic_onPrimaryContainer,
    secondary = arctic_secondary,
    onSecondary = arctic_onSecondary,
    secondaryContainer = arctic_secondaryContainer,
    onSecondaryContainer = arctic_onSecondaryContainer,
    tertiary = arctic_tertiary,
    onTertiary = arctic_onTertiary,
    tertiaryContainer = arctic_tertiaryContainer,
    onTertiaryContainer = arctic_onTertiaryContainer,
    error = arctic_error,
    onError = arctic_onError,
    errorContainer = arctic_errorContainer,
    onErrorContainer = arctic_onErrorContainer,
    background = arctic_background,
    onBackground = arctic_onBackground,
    surface = arctic_surface,
    onSurface = arctic_onSurface,
    surfaceVariant = arctic_surfaceVariant,
    onSurfaceVariant = arctic_onSurfaceVariant,
    outline = arctic_outline,
    outlineVariant = arctic_outlineVariant,
)

private val CyberColorScheme = darkColorScheme(
    primary = cyber_primary,
    onPrimary = cyber_onPrimary,
    primaryContainer = cyber_primaryContainer,
    onPrimaryContainer = cyber_onPrimaryContainer,
    secondary = cyber_secondary,
    onSecondary = cyber_onSecondary,
    secondaryContainer = cyber_secondaryContainer,
    onSecondaryContainer = cyber_onSecondaryContainer,
    tertiary = cyber_tertiary,
    onTertiary = cyber_onTertiary,
    tertiaryContainer = cyber_tertiaryContainer,
    onTertiaryContainer = cyber_onTertiaryContainer,
    error = cyber_error,
    onError = cyber_onError,
    errorContainer = cyber_errorContainer,
    onErrorContainer = cyber_onErrorContainer,
    background = cyber_background,
    onBackground = cyber_onBackground,
    surface = cyber_surface,
    onSurface = cyber_onSurface,
    surfaceVariant = cyber_surfaceVariant,
    onSurfaceVariant = cyber_onSurfaceVariant,
    outline = cyber_outline,
    outlineVariant = cyber_outlineVariant,
)

private val OceanColorScheme = darkColorScheme(
    primary = ocean_primary,
    onPrimary = ocean_onPrimary,
    primaryContainer = ocean_primaryContainer,
    onPrimaryContainer = ocean_onPrimaryContainer,
    secondary = ocean_secondary,
    onSecondary = ocean_onSecondary,
    secondaryContainer = ocean_secondaryContainer,
    onSecondaryContainer = ocean_onSecondaryContainer,
    tertiary = ocean_tertiary,
    onTertiary = ocean_onTertiary,
    tertiaryContainer = ocean_tertiaryContainer,
    onTertiaryContainer = ocean_onTertiaryContainer,
    error = ocean_error,
    onError = ocean_onError,
    errorContainer = ocean_errorContainer,
    onErrorContainer = ocean_onErrorContainer,
    background = ocean_background,
    onBackground = ocean_onBackground,
    surface = ocean_surface,
    onSurface = ocean_onSurface,
    surfaceVariant = ocean_surfaceVariant,
    onSurfaceVariant = ocean_onSurfaceVariant,
    outline = ocean_outline,
    outlineVariant = ocean_outlineVariant,
)

private val SakuraColorScheme = lightColorScheme(
    primary = sakura_primary,
    onPrimary = sakura_onPrimary,
    primaryContainer = sakura_primaryContainer,
    onPrimaryContainer = sakura_onPrimaryContainer,
    secondary = sakura_secondary,
    onSecondary = sakura_onSecondary,
    secondaryContainer = sakura_secondaryContainer,
    onSecondaryContainer = sakura_onSecondaryContainer,
    tertiary = sakura_tertiary,
    onTertiary = sakura_onTertiary,
    tertiaryContainer = sakura_tertiaryContainer,
    onTertiaryContainer = sakura_onTertiaryContainer,
    error = sakura_error,
    onError = sakura_onError,
    errorContainer = sakura_errorContainer,
    onErrorContainer = sakura_onErrorContainer,
    background = sakura_background,
    onBackground = sakura_onBackground,
    surface = sakura_surface,
    onSurface = sakura_onSurface,
    surfaceVariant = sakura_surfaceVariant,
    onSurfaceVariant = sakura_onSurfaceVariant,
    outline = sakura_outline,
    outlineVariant = sakura_outlineVariant,
)

// ── Gradient Color Sets ────────────────────────────

private val ObsidianGradients = GradientColors(
    gradientStart = obsidian_gradientStart,
    gradientEnd = obsidian_gradientEnd,
    glassBackground = Color(0xFF1E1E2E).copy(alpha = 0.6f),
    glassBorder = Color(0xFFA78BFA).copy(alpha = 0.15f),
    shimmerBase = Color(0xFF1E1E2E),
    shimmerHighlight = Color(0xFF2D2D44),
    cardGlow = Color(0xFFA78BFA).copy(alpha = 0.08f),
)

private val ArcticGradients = GradientColors(
    gradientStart = arctic_gradientStart,
    gradientEnd = arctic_gradientEnd,
    glassBackground = Color(0xFFFFFFFF).copy(alpha = 0.7f),
    glassBorder = Color(0xFF6366F1).copy(alpha = 0.12f),
    shimmerBase = Color(0xFFE2E8F0),
    shimmerHighlight = Color(0xFFF8FAFC),
    cardGlow = Color(0xFF6366F1).copy(alpha = 0.06f),
)

private val CyberGradients = GradientColors(
    gradientStart = cyber_gradientStart,
    gradientEnd = cyber_gradientEnd,
    glassBackground = Color(0xFF1A1A2E).copy(alpha = 0.65f),
    glassBorder = Color(0xFF06D6A0).copy(alpha = 0.2f),
    shimmerBase = Color(0xFF1A1A2E),
    shimmerHighlight = Color(0xFF2A2A44),
    cardGlow = Color(0xFF06D6A0).copy(alpha = 0.1f),
)

private val OceanGradients = GradientColors(
    gradientStart = ocean_gradientStart,
    gradientEnd = ocean_gradientEnd,
    glassBackground = Color(0xFF162438).copy(alpha = 0.6f),
    glassBorder = Color(0xFF2DD4BF).copy(alpha = 0.15f),
    shimmerBase = Color(0xFF162438),
    shimmerHighlight = Color(0xFF253848),
    cardGlow = Color(0xFF2DD4BF).copy(alpha = 0.08f),
)

private val SakuraGradients = GradientColors(
    gradientStart = sakura_gradientStart,
    gradientEnd = sakura_gradientEnd,
    glassBackground = Color(0xFFFFFFFF).copy(alpha = 0.75f),
    glassBorder = Color(0xFFF472B6).copy(alpha = 0.15f),
    shimmerBase = Color(0xFFFFF0F3),
    shimmerHighlight = Color(0xFFFFFFFF),
    cardGlow = Color(0xFFF472B6).copy(alpha = 0.06f),
)

// ── Shapes ─────────────────────────────────────────

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

// ── Animated Color Scheme ──────────────────────────

@Composable
private fun animateColorScheme(target: ColorScheme): ColorScheme {
    val duration = 400
    val spec = tween<Color>(durationMillis = duration)

    return ColorScheme(
        primary = animateColorAsState(target.primary, spec, label = "primary").value,
        onPrimary = animateColorAsState(target.onPrimary, spec, label = "onPrimary").value,
        primaryContainer = animateColorAsState(target.primaryContainer, spec, label = "primaryContainer").value,
        onPrimaryContainer = animateColorAsState(target.onPrimaryContainer, spec, label = "onPrimaryContainer").value,
        inversePrimary = animateColorAsState(target.inversePrimary, spec, label = "inversePrimary").value,
        secondary = animateColorAsState(target.secondary, spec, label = "secondary").value,
        onSecondary = animateColorAsState(target.onSecondary, spec, label = "onSecondary").value,
        secondaryContainer = animateColorAsState(target.secondaryContainer, spec, label = "secondaryContainer").value,
        onSecondaryContainer = animateColorAsState(target.onSecondaryContainer, spec, label = "onSecondaryContainer").value,
        tertiary = animateColorAsState(target.tertiary, spec, label = "tertiary").value,
        onTertiary = animateColorAsState(target.onTertiary, spec, label = "onTertiary").value,
        tertiaryContainer = animateColorAsState(target.tertiaryContainer, spec, label = "tertiaryContainer").value,
        onTertiaryContainer = animateColorAsState(target.onTertiaryContainer, spec, label = "onTertiaryContainer").value,
        background = animateColorAsState(target.background, spec, label = "background").value,
        onBackground = animateColorAsState(target.onBackground, spec, label = "onBackground").value,
        surface = animateColorAsState(target.surface, spec, label = "surface").value,
        onSurface = animateColorAsState(target.onSurface, spec, label = "onSurface").value,
        surfaceVariant = animateColorAsState(target.surfaceVariant, spec, label = "surfaceVariant").value,
        onSurfaceVariant = animateColorAsState(target.onSurfaceVariant, spec, label = "onSurfaceVariant").value,
        surfaceTint = animateColorAsState(target.surfaceTint, spec, label = "surfaceTint").value,
        inverseSurface = animateColorAsState(target.inverseSurface, spec, label = "inverseSurface").value,
        inverseOnSurface = animateColorAsState(target.inverseOnSurface, spec, label = "inverseOnSurface").value,
        error = animateColorAsState(target.error, spec, label = "error").value,
        onError = animateColorAsState(target.onError, spec, label = "onError").value,
        errorContainer = animateColorAsState(target.errorContainer, spec, label = "errorContainer").value,
        onErrorContainer = animateColorAsState(target.onErrorContainer, spec, label = "onErrorContainer").value,
        outline = animateColorAsState(target.outline, spec, label = "outline").value,
        outlineVariant = animateColorAsState(target.outlineVariant, spec, label = "outlineVariant").value,
        scrim = animateColorAsState(target.scrim, spec, label = "scrim").value,
        surfaceBright = animateColorAsState(target.surfaceBright, spec, label = "surfaceBright").value,
        surfaceDim = animateColorAsState(target.surfaceDim, spec, label = "surfaceDim").value,
        surfaceContainer = animateColorAsState(target.surfaceContainer, spec, label = "surfaceContainer").value,
        surfaceContainerHigh = animateColorAsState(target.surfaceContainerHigh, spec, label = "surfaceContainerHigh").value,
        surfaceContainerHighest = animateColorAsState(target.surfaceContainerHighest, spec, label = "surfaceContainerHighest").value,
        surfaceContainerLow = animateColorAsState(target.surfaceContainerLow, spec, label = "surfaceContainerLow").value,
        surfaceContainerLowest = animateColorAsState(target.surfaceContainerLowest, spec, label = "surfaceContainerLowest").value,
    )
}

// ── Theme Composable ───────────────────────────────

@Composable
fun VocabVaultTheme(
    appTheme: AppTheme = AppTheme.OBSIDIAN_DARK,
    content: @Composable () -> Unit
) {
    val targetColorScheme = when (appTheme) {
        AppTheme.OBSIDIAN_DARK -> ObsidianColorScheme
        AppTheme.ARCTIC_FROST -> ArcticColorScheme
        AppTheme.CYBER_NEON -> CyberColorScheme
        AppTheme.OCEAN_DEPTH -> OceanColorScheme
        AppTheme.SAKURA_BLOOM -> SakuraColorScheme
    }

    val gradientColors = when (appTheme) {
        AppTheme.OBSIDIAN_DARK -> ObsidianGradients
        AppTheme.ARCTIC_FROST -> ArcticGradients
        AppTheme.CYBER_NEON -> CyberGradients
        AppTheme.OCEAN_DEPTH -> OceanGradients
        AppTheme.SAKURA_BLOOM -> SakuraGradients
    }

    val animatedColorScheme = animateColorScheme(targetColorScheme)

    CompositionLocalProvider(
        LocalAppTheme provides appTheme,
        LocalGradientColors provides gradientColors
    ) {
        MaterialTheme(
            colorScheme = animatedColorScheme,
            typography = AppTypography,
            shapes = AppShapes,
            content = content
        )
    }
}