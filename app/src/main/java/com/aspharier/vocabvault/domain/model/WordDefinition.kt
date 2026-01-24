package com.aspharier.vocabvault.domain.model

data class WordDefinition(
    val word: String,
    val phonetic: String?,
    val partOfSpeech: String,
    val definition: String
)
