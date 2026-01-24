package com.aspharier.vocabvault.di

import com.aspharier.vocabvault.data.repository.WordRepositoryImpl
import com.aspharier.vocabvault.domain.repository.WordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindWordRepository(
        impl: WordRepositoryImpl
    ): WordRepository
}