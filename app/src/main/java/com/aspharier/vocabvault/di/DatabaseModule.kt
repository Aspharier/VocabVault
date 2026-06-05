package com.aspharier.vocabvault.di

import android.content.Context
import androidx.room.Room
import com.aspharier.vocabvault.data.local.dao.SavedWordDao
import com.aspharier.vocabvault.data.local.database.VocabDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): VocabDatabase =
        Room.databaseBuilder(
            context,
            VocabDatabase::class.java,
            "vocab_db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideDao(db: VocabDatabase): SavedWordDao =
        db.savedWordDao()
}