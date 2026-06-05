package com.aspharier.vocabvault.presentation.home

import com.aspharier.vocabvault.domain.model.WordDefinition

data class HomeUiState(
    val wordOfTheDay: WordDefinition? = null,
    val streakCount: Int = 5,
    val xpCount: Int = 45,
    val wordsDueCount: Int = 0,
    val totalWordsCount: Int = 0,
    val isLoading: Boolean = false,
    val recommendations: List<WordDefinition> = emptyList(),
    val challengeProgress: Float = 0f,
    val challengeStatus: String = "0 of 3 words saved. Save 3 new words to complete today's challenge!"
)
