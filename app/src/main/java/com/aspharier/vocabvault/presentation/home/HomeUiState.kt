package com.aspharier.vocabvault.presentation.home

import com.aspharier.vocabvault.domain.model.WordDefinition

data class HomeUiState(
    val searchQuery: String = "",
    val recentSearches: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val wordResult: WordDefinition? = null,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)
