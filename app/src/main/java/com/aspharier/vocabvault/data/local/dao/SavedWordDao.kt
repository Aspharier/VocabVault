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

    @Query("SELECT EXISTS(SELECT 1 FROM saved_words WHERE word = :word)")
    suspend fun isWordSaved(word: String): Boolean
}