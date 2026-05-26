package com.aspharier.vocabvault.presentation.dictionary

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.aspharier.vocabvault.domain.model.WordDefinition
import com.aspharier.vocabvault.presentation.common.AnimationUtils
import com.aspharier.vocabvault.presentation.components.GlassCard
import com.aspharier.vocabvault.presentation.components.ParticleBackground
import com.aspharier.vocabvault.presentation.theme.LocalGradientColors

@Composable
fun DictionaryScreen(
    viewModel: DictionaryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedWord by remember { mutableStateOf<WordDefinition?>(null) }
    val gradientColors = LocalGradientColors.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Background particles
        ParticleBackground(particleCount = 6)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            // ── Header ───────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Vault",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // Word count badge
                AnimatedVisibility(
                    visible = uiState.words.isNotEmpty(),
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = gradientColors.gradientStart.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "${uiState.words.size} words",
                            style = MaterialTheme.typography.labelMedium,
                            color = gradientColors.gradientStart,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // ── Content ──────────────────────────────
            AnimatedContent(
                targetState = uiState.words.isEmpty(),
                transitionSpec = {
                    fadeIn(tween(300)) togetherWith fadeOut(tween(200))
                },
                label = "dictionary_content"
            ) { isEmpty ->
                if (isEmpty) {
                    EmptyDictionaryState(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(
                            top = 4.dp,
                            bottom = 100.dp
                        )
                    ) {
                        itemsIndexed(
                            items = uiState.words,
                            key = { _, word -> word.word }
                        ) { index, word ->
                            DictionaryItem(
                                word = word,
                                index = index,
                                onDelete = { viewModel.deleteWord(word.word) },
                                onClick = { selectedWord = word }
                            )
                        }
                    }
                }
            }
        }

        // Word detail dialog
        selectedWord?.let { word ->
            WordDetailDialog(
                word = word,
                onDismiss = { selectedWord = null }
            )
        }
    }
}

@Composable
fun DictionaryItem(
    word: WordDefinition,
    index: Int,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    val gradientColors = LocalGradientColors.current

    // Staggered entrance animation
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val delay = AnimationUtils.staggerDelay(index)

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            tween(AnimationUtils.DURATION_MEDIUM, delayMillis = delay)
        ) + slideInVertically(
            tween(AnimationUtils.DURATION_MEDIUM, delayMillis = delay),
            initialOffsetY = { it / 3 }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(gradientColors.glassBackground)
                .border(
                    width = 1.dp,
                    color = gradientColors.glassBorder,
                    shape = RoundedCornerShape(18.dp)
                )
        ) {
            // Gradient accent strip
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(IntrinsicSize.Max)
                    .defaultMinSize(minHeight = 70.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                gradientColors.gradientStart,
                                gradientColors.gradientEnd
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 12.dp)
                ) {
                    // Clickable word title
                    TextButton(
                        onClick = onClick,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = word.word,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Part of speech badge
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = gradientColors.gradientStart.copy(alpha = 0.08f),
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = word.partOfSpeech,
                            style = MaterialTheme.typography.labelSmall,
                            color = gradientColors.gradientStart.copy(alpha = 0.8f),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = word.definition,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Delete button
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyDictionaryState(
    modifier: Modifier = Modifier
) {
    val gradientColors = LocalGradientColors.current

    val infiniteTransition = rememberInfiniteTransition(label = "empty_vault")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "vault_scale"
    )

    val iconAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "vault_alpha"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .scale(scale),
                tint = gradientColors.gradientStart.copy(alpha = iconAlpha)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Your vault is empty",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Search and save words to build\nyour personal vocabulary collection",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun WordDetailDialog(
    word: WordDefinition,
    onDismiss: () -> Unit
) {
    val gradientColors = LocalGradientColors.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        // Scale-in animation
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { visible = true }

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(200)) + scaleIn(
                tween(300),
                initialScale = 0.85f
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(gradientColors.glassBackground)
                    .border(
                        width = 1.dp,
                        color = gradientColors.glassBorder,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
            ) {
                Column(
                    modifier = Modifier.padding(28.dp)
                ) {
                    // Word title
                    Text(
                        text = word.word,
                        style = MaterialTheme.typography.headlineLarge,
                        color = gradientColors.gradientStart,
                        fontWeight = FontWeight.Bold
                    )

                    // Phonetic
                    word.phonetic?.let { phonetic ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = phonetic,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            fontWeight = FontWeight.Light
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Gradient divider
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(3.dp)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        gradientColors.gradientStart,
                                        gradientColors.gradientEnd
                                    )
                                )
                            )
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    // Part of speech
                    Text(
                        text = "Part of Speech",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = gradientColors.gradientStart.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = word.partOfSpeech,
                            style = MaterialTheme.typography.bodyLarge,
                            color = gradientColors.gradientStart,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    // Definition
                    Text(
                        text = "Definition",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = word.definition,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 1.5.em
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // Close button
                    TextButton(
                        onClick = onDismiss,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            "Close",
                            style = MaterialTheme.typography.labelLarge,
                            color = gradientColors.gradientStart
                        )
                    }
                }
            }
        }
    }
}
