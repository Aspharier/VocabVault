package com.aspharier.vocabvault.presentation.home

import com.aspharier.vocabvault.domain.model.WordDefinition

data class HomeUiState(
    val wordOfTheDay: WordDefinition? = null,
    val streakCount: Int = 5,
    val wordsDueCount: Int = 0,
    val totalWordsCount: Int = 0,
    val isLoading: Boolean = false,
    val recommendations: List<WordDefinition> = emptyList()
)
