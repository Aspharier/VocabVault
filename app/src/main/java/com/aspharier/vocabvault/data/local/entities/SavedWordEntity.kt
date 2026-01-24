package com.aspharier.vocabvault.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_words")
data class SavedWordEntity(
    @PrimaryKey val word: String,
    val phonetic: String?,
    val partOfSpeech: String,
    val definition: String,
    val savedAt: Long
)
