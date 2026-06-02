package com.aspharier.vocabvault.presentation.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aspharier.vocabvault.domain.model.WordDefinition

@OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class
)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is HomeUiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.38f),
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp)
                    .padding(top = 28.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                HomeHero()

                SearchCommandCard(
                    query = uiState.searchQuery,
                    isLoading = uiState.isLoading,
                    onQueryChange = viewModel::onSearchQueryChange,
                    onSearch = viewModel::onSearchClicked
                )

                RecentSearchPanel(
                    recentSearches = uiState.recentSearches,
                    onRecentSearchClicked = viewModel::onRecentSearchClicked
                )

                AnimatedContent(
                    targetState = when {
                        uiState.isLoading -> "loading"
                        uiState.errorMessage != null -> "error"
                        uiState.wordResult != null -> "result"
                        else -> "guide"
                    },
                    transitionSpec = {
                        (fadeIn(tween(220)) + slideInVertically { it / 6 })
                            .togetherWith(fadeOut(tween(160)) + slideOutVertically { -it / 8 })
                            .using(SizeTransform(clip = false))
                    },
                    label = "home_result_content"
                ) { state ->
                    when (state) {
                        "loading" -> LookupLoadingCard()
                        "error" -> GuideCard(
                            title = uiState.errorMessage ?: "Word not found",
                            message = "Try a different spelling or search another word.",
                            tone = GuideTone.Error
                        )
                        "result" -> WordResultCard(
                            word = uiState.wordResult!!,
                            isSaved = uiState.isSaved,
                            onSave = viewModel::onSavedClicked
                        )
                        else -> GuideCard(
                            title = "Ready when a word catches your eye",
                            message = "Search anything you read, then keep the useful words in your vault."
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeHero() {
    Text(
        text = "VocabVault",
        style = MaterialTheme.typography.displaySmall,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun SearchCommandCard(
    query: String,
    isLoading: Boolean,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Lookup",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "One word at a time",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    modifier = Modifier.size(42.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(21.dp)
                        )
                    }
                }
            }

            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                placeholder = { Text("Try “eloquent”") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch() })
            )

            Button(
                onClick = onSearch,
                enabled = query.isNotBlank() && !isLoading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 15.dp)
            ) {
                Text(if (isLoading) "Searching..." else "Find meaning")
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RecentSearchPanel(
    recentSearches: List<String>,
    onRecentSearchClicked: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (recentSearches.isEmpty()) "No searches yet" else "${recentSearches.size} saved",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        AnimatedVisibility(
            visible = recentSearches.isNotEmpty(),
            enter = fadeIn(tween(180)) + scaleIn(tween(220), initialScale = 0.96f),
            exit = fadeOut(tween(140)) + scaleOut(tween(140), targetScale = 0.96f)
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                recentSearches.forEach { word ->
                    AssistChip(
                        onClick = { onRecentSearchClicked(word) },
                        label = { Text(word) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LookupLoadingCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(22.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(modifier = Modifier.size(28.dp), strokeWidth = 3.dp)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Searching dictionary",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Finding the cleanest definition for you.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private enum class GuideTone {
    Neutral,
    Error
}

@Composable
private fun GuideCard(
    title: String,
    message: String,
    tone: GuideTone = GuideTone.Neutral
) {
    val containerColor = if (tone == GuideTone.Error) {
        MaterialTheme.colorScheme.errorContainer
    } else {
        MaterialTheme.colorScheme.surface
    }
    val contentColor = if (tone == GuideTone.Error) {
        MaterialTheme.colorScheme.onErrorContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = contentColor,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor.copy(alpha = 0.74f)
            )
        }
    }
}

@Composable
private fun WordResultCard(
    word: WordDefinition,
    isSaved: Boolean,
    onSave: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = word.word,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        fontWeight = FontWeight.Bold
                    )
                    word.phonetic?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.68f)
                        )
                    }
                }

                Surface(
                    color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.12f),
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    shape = CircleShape
                ) {
                    Text(
                        text = word.partOfSpeech.take(1).uppercase(),
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Surface(
                color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.10f),
                contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = word.partOfSpeech,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Text(
                text = word.definition,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.inverseOnSurface,
                textAlign = TextAlign.Start
            )

            FilledTonalButton(
                onClick = onSave,
                enabled = !isSaved,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = 13.dp)
            ) {
                Icon(
                    imageVector = if (isSaved) Icons.Default.Star else Icons.Outlined.Star,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(if (isSaved) "Already in vault" else "Save this word")
            }
        }
    }
}
