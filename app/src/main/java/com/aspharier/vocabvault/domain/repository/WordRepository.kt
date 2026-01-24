package com.aspharier.vocabvault.domain.repository

import com.aspharier.vocabvault.domain.model.WordDefinition
import kotlinx.coroutines.flow.Flow

interface WordRepository {

    suspend fun saveWord(word: WordDefinition)

    fun getSavedWords(): Flow<List<WordDefinition>>

    suspend fun deleteWord(word: String)

    suspend fun isWordSaved(word: String): Boolean
}