package com.aspharier.vocabvault.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aspharier.vocabvault.data.local.entities.SavedWordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedWordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: SavedWordEntity)

    @Query("SELECT * FROM saved_words ORDER BY word ASC")
    fun getAllWords(): Flow<List<SavedWordEntity>>

    @Query("DELETE FROM saved_words WHERE  word = :word")
    suspend fun deleteWord(word: String)

    @Query("DELETE FROM saved_words")
    suspend fun deleteAllWords()

    @Query("SELECT EXISTS(SELECT 1 FROM saved_words WHERE word = :word)")
    suspend fun isWordSaved(word: String): Boolean

    @Query("SELECT * FROM saved_words WHERE nextReviewAt <= :currentTime AND leitnerBox < 4 ORDER BY savedAt ASC")
    fun getWordsDueForReview(currentTime: Long): Flow<List<SavedWordEntity>>

    @Query("UPDATE saved_words SET leitnerBox = :newBox, nextReviewAt = :nextReview, lastReviewedAt = :lastReviewed WHERE word = :word")
    suspend fun updateLeitnerState(word: String, newBox: Int, nextReview: Long, lastReviewed: Long)

    @Query("SELECT COUNT(*) FROM saved_words WHERE nextReviewAt <= :currentTime AND leitnerBox < 4")
    fun getWordsDueForReviewCount(currentTime: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM saved_words")
    fun getTotalWordsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM saved_words WHERE leitnerBox = 4")
    fun getMasteredWordsCount(): Flow<Int>
}