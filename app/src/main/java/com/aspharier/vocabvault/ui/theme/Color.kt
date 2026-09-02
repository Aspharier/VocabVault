package com.aspharier.vocabvault.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Editorial color palette tokens directly from vocabvault-ui.html
 */
@Immutable
data class VocabColors(
    val bg: Color,
    val bgSunken: Color,
    val raise: Color,
    val ink: Color,
    val ink2: Color,
    val ink3: Color,
    val rule: Color,
    val rule2: Color,
    val accent: Color,
    val accentInk: Color,
    val accentWash: Color,
    val danger: Color,
    val sel: Color,
    val isDark: Boolean
)

// Dark: near-black ink field, warm parchment accent
val DarkVocabColors = VocabColors(
    bg = Color(0xFF0D0D0F),
    bgSunken = Color(0xFF08080A),
    raise = Color(0xFF141417),
    ink = Color(0xFFF2F0EC),
    ink2 = Color(0xFFA4A29C),
    ink3 = Color(0xFF6A6862),
    rule = Color(0xFF232327),
    rule2 = Color(0xFF2F2F34),
    accent = Color(0xFFD9A95F),
    accentInk = Color(0xFF1A1408),
    accentWash = Color(0x21D9A95F), // rgba(217,169,95,.13)
    danger = Color(0xFFE0705F),
    sel = Color(0x38D9A95F),        // rgba(217,169,95,.22)
    isDark = true
)

// Light: warm paper, ink black, same accent family
val LightVocabColors = VocabColors(
    bg = Color(0xFFF7F5F0),
    bgSunken = Color(0xFFEFECE5),
    raise = Color(0xFFFFFEFB),
    ink = Color(0xFF171613),
    ink2 = Color(0xFF5F5D56),
    ink3 = Color(0xFF918E85),
    rule = Color(0xFFDEDAD1),
    rule2 = Color(0xFFCEC9BE),
    accent = Color(0xFF9A6B17),
    accentInk = Color(0xFFFFFDF7),
    accentWash = Color(0x179A6B17), // rgba(154,107,23,.09)
    danger = Color(0xFFA83A28),
    sel = Color(0x299A6B17),        // rgba(154,107,23,.16)
    isDark = false
)