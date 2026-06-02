package com.aspharier.vocabvault.presentation.dictionary

import com.aspharier.vocabvault.domain.model.WordDefinition

data class DictionaryUiState(
    val words: List<WordDefinition> = emptyList(),
    val searchQuery: String = "",
    val sortMode: VaultSortMode = VaultSortMode.Newest
)

enum class VaultSortMode(val label: String) {
    Newest("Newest"),
    Alphabetical("A-Z"),
    PartOfSpeech("Type")
}
