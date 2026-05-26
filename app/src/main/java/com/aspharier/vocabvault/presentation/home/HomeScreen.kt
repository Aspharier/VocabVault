package com.aspharier.vocabvault.presentation.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aspharier.vocabvault.presentation.common.AnimationUtils
import com.aspharier.vocabvault.presentation.components.*
import com.aspharier.vocabvault.presentation.theme.LocalGradientColors

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val gradientColors = LocalGradientColors.current
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is HomeUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Particle background
            ParticleBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .padding(horizontal = 20.dp)
                    .padding(top = 28.dp, bottom = 16.dp)
                    .verticalScroll(scrollState)
            ) {
                // ── Animated Header ──────────────────────
                var headerVisible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) { headerVisible = true }

                AnimatedVisibility(
                    visible = headerVisible,
                    enter = fadeIn(tween(500)) + slideInVertically(
                        tween(500),
                        initialOffsetY = { -it / 4 }
                    )
                ) {
                    Column {
                        // Gradient app title
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                gradientColors.gradientStart,
                                                gradientColors.gradientEnd
                                            )
                                        ),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 32.sp
                                    )
                                ) {
                                    append("VocabVault")
                                }
                            },
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        Text(
                            text = "Discover meanings, expand your world",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier.padding(bottom = 28.dp)
                        )
                    }
                }

                // ── Search Bar ───────────────────────────
                AnimatedSearchBar(
                    value = uiState.searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    placeholder = "Enter a word to search..."
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ── Search Button ────────────────────────
                GradientButton(
                    text = "Search",
                    onClick = viewModel::onSearchClicked,
                    enabled = uiState.searchQuery.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = if (uiState.searchQuery.isNotEmpty())
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                        )
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // ── Content State ────────────────────────
                AnimatedContent(
                    targetState = when {
                        uiState.isLoading -> "loading"
                        uiState.errorMessage != null -> "error"
                        uiState.wordResult != null -> "result"
                        else -> "idle"
                    },
                    transitionSpec = {
                        fadeIn(tween(300)) togetherWith fadeOut(tween(200))
                    },
                    label = "content_state"
                ) { state ->
                    when (state) {
                        "loading" -> {
                            GlassCard(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                ShimmerLoading()
                            }
                        }

                        "error" -> {
                            // Error card with shake animation
                            val shake = remember { Animatable(0f) }
                            LaunchedEffect(uiState.errorMessage) {
                                shake.animateTo(
                                    targetValue = 0f,
                                    animationSpec = keyframes {
                                        durationMillis = 400
                                        0f at 0
                                        -8f at 50
                                        8f at 100
                                        -6f at 150
                                        6f at 200
                                        -4f at 250
                                        4f at 300
                                        0f at 400
                                    }
                                )
                            }

                            GlassCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .offset(x = shake.value.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "😔",
                                        fontSize = 32.sp,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    Text(
                                        text = uiState.errorMessage ?: "An error occurred",
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Try a different word or check your connection",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                            alpha = 0.6f
                                        ),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                        "result" -> {
                            val result = uiState.wordResult!!

                            AnimatedWordCard {
                                // Word title
                                Text(
                                    text = result.word,
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = gradientColors.gradientStart,
                                    fontWeight = FontWeight.Bold
                                )

                                // Phonetic
                                result.phonetic?.let { phonetic ->
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = phonetic,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                            alpha = 0.6f
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                // Gradient divider
                                Box(
                                    modifier = Modifier
                                        .width(60.dp)
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

                                Spacer(modifier = Modifier.height(16.dp))

                                // Part of speech badge
                                Surface(
                                    shape = MaterialTheme.shapes.small,
                                    color = gradientColors.gradientStart.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = result.partOfSpeech.uppercase(),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = gradientColors.gradientStart,
                                        modifier = Modifier.padding(
                                            horizontal = 10.dp,
                                            vertical = 4.dp
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Definition
                                Text(
                                    text = "Definition",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                        alpha = 0.7f
                                    )
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = result.definition,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                // Save Button
                                val saveScale by animateFloatAsState(
                                    targetValue = if (uiState.isSaved) 1.05f else 1f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    ),
                                    label = "save_scale"
                                )

                                GradientButton(
                                    text = if (uiState.isSaved) "Saved ✓" else "Save to Vault",
                                    onClick = viewModel::onSavedClicked,
                                    enabled = !uiState.isSaved,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .scale(saveScale),
                                    leadingIcon = {
                                        Icon(
                                            imageVector = if (uiState.isSaved) Icons.Default.Star
                                            else Icons.Outlined.Star,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp),
                                            tint = if (!uiState.isSaved)
                                                MaterialTheme.colorScheme.onPrimary
                                            else
                                                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                                        )
                                    }
                                )
                            }
                        }

                        "idle" -> {
                            // Empty state
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(vertical = 48.dp)
                                ) {
                                    // Animated search icon
                                    val infiniteTransition =
                                        rememberInfiniteTransition(label = "idle")
                                    val iconScale by infiniteTransition.animateFloat(
                                        initialValue = 1f,
                                        targetValue = 1.08f,
                                        animationSpec = infiniteRepeatable(
                                            animation = tween(2500, easing = FastOutSlowInEasing),
                                            repeatMode = RepeatMode.Reverse
                                        ),
                                        label = "idle_scale"
                                    )
                                    val iconAlpha by infiniteTransition.animateFloat(
                                        initialValue = 0.2f,
                                        targetValue = 0.35f,
                                        animationSpec = infiniteRepeatable(
                                            animation = tween(2500, easing = FastOutSlowInEasing),
                                            repeatMode = RepeatMode.Reverse
                                        ),
                                        label = "idle_alpha"
                                    )

                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(72.dp)
                                            .scale(iconScale),
                                        tint = gradientColors.gradientStart.copy(alpha = iconAlpha)
                                    )

                                    Spacer(modifier = Modifier.height(20.dp))

                                    Text(
                                        "Start exploring",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        "Enter a word above to unlock its meaning",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                            alpha = 0.5f
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
