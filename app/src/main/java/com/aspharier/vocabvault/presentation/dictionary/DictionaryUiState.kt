package com.aspharier.vocabvault.presentation.dictionary

import com.aspharier.vocabvault.domain.model.WordDefinition

data class DictionaryUiState(
    val words: List<WordDefinition> = emptyList()
)
