package com.aspharier.vocabvault.domain.repository

import com.aspharier.vocabvault.domain.model.WordDefinition
import kotlinx.coroutines.flow.Flow

interface WordRepository {

    suspend fun saveWord(word: WordDefinition)

    fun getSavedWords(): Flow<List<WordDefinition>>

    suspend fun deleteWord(word: String)

    suspend fun isWordSaved(word: String): Boolean

    fun getWordsDueForReview(currentTime: Long): Flow<List<WordDefinition>>

    suspend fun updateLeitnerState(word: String, newBox: Int, nextReview: Long, lastReviewed: Long)

    fun getWordsDueForReviewCount(currentTime: Long): Flow<Int>

    fun getTotalWordsCount(): Flow<Int>

    fun getMasteredWordsCount(): Flow<Int>
}