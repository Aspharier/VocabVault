package com.aspharier.vocabvault.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aspharier.vocabvault.data.local.dao.SavedWordDao
import com.aspharier.vocabvault.data.local.entities.SavedWordEntity

@Database(
    entities = [SavedWordEntity::class],
    version = 1
)
abstract class VocabDatabase : RoomDatabase() {
    abstract fun savedWordDao(): SavedWordDao
}