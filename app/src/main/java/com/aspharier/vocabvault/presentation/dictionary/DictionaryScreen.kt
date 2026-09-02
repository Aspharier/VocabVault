package com.aspharier.vocabvault.presentation.dictionary

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aspharier.vocabvault.domain.model.WordDefinition
import com.aspharier.vocabvault.presentation.common.rememberTextToSpeech
import com.aspharier.vocabvault.presentation.theme.DMMono
import com.aspharier.vocabvault.presentation.theme.SerifFont
import com.aspharier.vocabvault.presentation.theme.VocabTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DictionaryScreen(
    viewModel: DictionaryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val tts = rememberTextToSpeech()

    val colors = VocabTheme.colors
    val scale = VocabTheme.scale

    var expandedWord by remember { mutableStateOf<String?>(null) }
    var toastWord by remember { mutableStateOf<WordDefinition?>(null) }

    LaunchedEffect(toastWord) {
        if (toastWord != null) {
            delay(4000)
            toastWord = null
        }
    }

    // Sort list according to sort mode
    val sortedWords = remember(uiState.words, uiState.sortMode) {
        when (uiState.sortMode) {
            VaultSortMode.Newest -> uiState.words.sortedByDescending { it.savedAt }
            VaultSortMode.Alphabetical -> uiState.words.sortedBy { it.word.lowercase() }
            VaultSortMode.PartOfSpeech -> uiState.words.sortedWith(
                compareBy<WordDefinition> { it.partOfSpeech.lowercase() }
                    .thenBy { it.word.lowercase() }
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bg)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
        ) {
            // Header: Kicker + Title + Subtitle
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp)
                ) {
                    Text(
                        text = "COLLECTED",
                        fontFamily = DMMono,
                        fontSize = (9.5f * scale).sp,
                        letterSpacing = 0.22.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.ink3
                    )
                    Spacer(modifier = Modifier.height(9.dp))

                    val titleText = buildAnnotatedString {
                        append("My ")
                        withStyle(SpanStyle(fontStyle = FontStyle.Italic, color = colors.accent)) {
                            append("vault")
                        }
                    }
                    Text(
                        text = titleText,
                        fontFamily = SerifFont,
                        fontSize = (30f * scale).sp,
                        letterSpacing = (-0.02).sp,
                        color = colors.ink,
                        lineHeight = (32f * scale).sp
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(
                        text = "${uiState.words.size} word${if (uiState.words.size == 1) "" else "s"} kept",
                        fontFamily = DMMono,
                        fontSize = (11f * scale).sp,
                        color = colors.ink3,
                        letterSpacing = 0.02.sp
                    )
                }
            }

            // Sortbar: SORT Newest | A-Z | Type
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 13.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    Text(
                        text = "SORT",
                        fontFamily = DMMono,
                        fontSize = (9f * scale).sp,
                        letterSpacing = 0.18.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.ink3
                    )

                    VaultSortMode.entries.forEach { mode ->
                        val isSelected = uiState.sortMode == mode
                        Column(
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    viewModel.onSortModeChange(mode)
                                    expandedWord = null
                                }
                        ) {
                            Text(
                                text = mode.label,
                                fontFamily = DMMono,
                                fontSize = (11f * scale).sp,
                                letterSpacing = 0.04.sp,
                                color = if (isSelected) colors.ink else colors.ink3,
                                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Box(
                                modifier = Modifier
                                    .width((mode.label.length * 7).dp)
                                    .height(1.dp)
                                    .background(if (isSelected) colors.accent else colors.bg)
                            )
                        }
                    }
                }

                // Hairline divider under sort bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(colors.rule)
                )
            }

            // Empty state
            if (sortedWords.isEmpty()) {
                item {
                    VaultEmptyState(scale = scale)
                }
            } else {
                var lastIndexKey: String? = null

                items(sortedWords, key = { it.word }) { item ->
                    val currentKey = when (uiState.sortMode) {
                        VaultSortMode.Alphabetical -> item.word.firstOrNull()?.uppercaseChar()?.toString()
                        VaultSortMode.PartOfSpeech -> item.partOfSpeech.uppercase()
                        else -> null
                    }

                    // Section Divider
                    if (currentKey != null && currentKey != lastIndexKey) {
                        lastIndexKey = currentKey
                        Text(
                            text = currentKey,
                            fontFamily = DMMono,
                            fontSize = (9.5f * scale).sp,
                            letterSpacing = 0.2.sp,
                            color = colors.ink3,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 19.dp, bottom = 8.dp)
                        )
                    }

                    val isExpanded = expandedWord == item.word

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        // Main row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    expandedWord = if (isExpanded) null else item.word
                                }
                                .padding(vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.word,
                                fontFamily = SerifFont,
                                fontSize = (19f * scale).sp,
                                letterSpacing = (-0.015).sp,
                                color = colors.ink
                            )

                            Spacer(modifier = Modifier.width(11.dp))

                            val shortPos = if (item.partOfSpeech.length > 4) {
                                "${item.partOfSpeech.take(4)}."
                            } else {
                                "${item.partOfSpeech}."
                            }

                            Text(
                                text = shortPos,
                                fontFamily = SerifFont,
                                fontStyle = FontStyle.Italic,
                                fontSize = (11.5f * scale).sp,
                                color = colors.accent
                            )

                            Spacer(modifier = Modifier.width(11.dp))

                            if (!isExpanded) {
                                Text(
                                    text = item.definition,
                                    fontFamily = DMMono,
                                    fontSize = (11.5f * scale).sp,
                                    color = colors.ink3,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Expanded detail
                        AnimatedVisibility(
                            visible = isExpanded,
                            enter = expandVertically(tween(250)) + fadeIn(tween(250)),
                            exit = shrinkVertically(tween(200)) + fadeOut(tween(200))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 17.dp)
                            ) {
                                Text(
                                    text = item.definition,
                                    fontFamily = DMMono,
                                    fontSize = (13.5f * scale).sp,
                                    lineHeight = (21f * scale).sp,
                                    color = colors.ink,
                                    modifier = Modifier.padding(bottom = 13.dp)
                                )

                                val savedDateStr = remember(item.savedAt) {
                                    val date = if (item.savedAt > 0) Date(item.savedAt) else Date()
                                    SimpleDateFormat("d MMM", Locale.getDefault()).format(date)
                                }

                                val phoneticPart = if (!item.phonetic.isNullOrBlank()) "${item.phonetic} · " else ""
                                Text(
                                    text = "$phoneticPart${item.partOfSpeech} · SAVED $savedDateStr".uppercase(),
                                    fontFamily = DMMono,
                                    fontSize = (10f * scale).sp,
                                    letterSpacing = 0.1.sp,
                                    color = colors.ink3,
                                    modifier = Modifier.padding(bottom = 14.dp)
                                )

                                // Action buttons: Say, Copy, Delete
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Say action
                                    Row(
                                        modifier = Modifier.clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) {
                                            tts.speak(item.word)
                                        },
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(text = "🔊", fontSize = (12f * scale).sp)
                                        Text(
                                            text = "SAY",
                                            fontFamily = DMMono,
                                            fontSize = (10f * scale).sp,
                                            letterSpacing = 0.14.sp,
                                            color = colors.ink3
                                        )
                                    }

                                    // Copy action
                                    Row(
                                        modifier = Modifier.clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val clip = ClipData.newPlainText("word_definition", "${item.word}: ${item.definition}")
                                            clipboard.setPrimaryClip(clip)
                                        },
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(text = "📋", fontSize = (12f * scale).sp)
                                        Text(
                                            text = "COPY",
                                            fontFamily = DMMono,
                                            fontSize = (10f * scale).sp,
                                            letterSpacing = 0.14.sp,
                                            color = colors.ink3
                                        )
                                    }

                                    // Delete action
                                    Row(
                                        modifier = Modifier.clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) {
                                            toastWord = item
                                            viewModel.deleteWord(item.word)
                                            if (expandedWord == item.word) {
                                                expandedWord = null
                                            }
                                        },
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = colors.danger,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Text(
                                            text = "DELETE",
                                            fontFamily = DMMono,
                                            fontSize = (10f * scale).sp,
                                            letterSpacing = 0.14.sp,
                                            color = colors.danger
                                        )
                                    }
                                }
                            }
                        }

                        // Hairline row separator
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(colors.rule)
                        )
                    }
                }
            }

            // Bottom clearance for nav bar
            item {
                Spacer(modifier = Modifier.height(104.dp))
            }
        }

        // Floating Undo Toast (above nav bar)
        AnimatedVisibility(
            visible = toastWord != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 76.dp, start = 16.dp, end = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(3.dp))
                    .background(colors.raise, RoundedCornerShape(3.dp))
                    .border(1.dp, colors.rule2, RoundedCornerShape(3.dp))
                    .padding(horizontal = 16.dp, vertical = 13.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val deleted = toastWord
                    val toastText = buildAnnotatedString {
                        withStyle(SpanStyle(fontFamily = SerifFont, fontStyle = FontStyle.Italic, color = colors.ink)) {
                            append(deleted?.word ?: "")
                        }
                        append(" removed")
                    }
                    Text(
                        text = toastText,
                        fontFamily = DMMono,
                        fontSize = (11.5f * scale).sp,
                        color = colors.ink2
                    )

                    Text(
                        text = "UNDO",
                        fontFamily = DMMono,
                        fontSize = (10f * scale).sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.16.sp,
                        color = colors.accent,
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                val wordToRestore = toastWord
                                if (wordToRestore != null) {
                                    viewModel.restoreWord(wordToRestore)
                                    toastWord = null
                                }
                            }
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun VaultEmptyState(scale: Float) {
    val colors = VocabTheme.colors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 74.dp, bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "∅",
            fontFamily = SerifFont,
            fontSize = (52f * scale).sp,
            color = colors.rule2,
            lineHeight = (54f * scale).sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Nothing saved yet",
            fontFamily = SerifFont,
            fontSize = (17f * scale).sp,
            fontWeight = FontWeight.Normal,
            color = colors.ink2
        )
        Spacer(modifier = Modifier.height(7.dp))
        Text(
            text = "Words you save from Search collect here.",
            fontFamily = DMMono,
            fontSize = (11.5f * scale).sp,
            lineHeight = (18f * scale).sp,
            color = colors.ink3,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}
