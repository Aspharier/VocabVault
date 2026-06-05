package com.aspharier.vocabvault.domain.model

data class WordDefinition(
    val word: String,
    val phonetic: String?,
    val partOfSpeech: String,
    val definition: String,
    val savedAt: Long = 0L,
    val leitnerBox: Int = 1,
    val nextReviewAt: Long = 0L,
    val lastReviewedAt: Long = 0L
)
