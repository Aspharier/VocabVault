package com.aspharier.vocabvault.data.remote.dto

data class WordResponseDto(
    val word: String,
    val phonetic: String?,
    val meanings: List<MeaningDto>
)

data class MeaningDto(
    val partOfSpeech: String,
    val definitions: List<DefinitionDto>
)

data class DefinitionDto(
    val definition: String,
    val example: String
)
