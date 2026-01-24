package com.aspharier.vocabvault.data.repository

import com.aspharier.vocabvault.data.local.dao.SavedWordDao
import com.aspharier.vocabvault.data.local.entities.SavedWordEntity
import com.aspharier.vocabvault.domain.model.WordDefinition
import com.aspharier.vocabvault.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WordRepositoryImpl @Inject constructor(
    private val dao: SavedWordDao
) : WordRepository {

    override suspend fun saveWord(word: WordDefinition) {
        dao.insertWord(
            SavedWordEntity(
                word = word.word,
                phonetic = word.phonetic,
                partOfSpeech = word.partOfSpeech,
                definition = word.definition,
                savedAt = System.currentTimeMillis()
            )
        )
    }

    override fun getSavedWords(): Flow<List<WordDefinition>> =
        dao.getAllWords().map { list ->
            list.map {
                WordDefinition(
                    word = it.word,
                    phonetic = it.phonetic,
                    partOfSpeech = it.partOfSpeech,
                    definition = it.definition
                )
            }
        }

    override suspend fun deleteWord(word: String) {
        dao.deleteWord(word)
    }

    override suspend fun isWordSaved(word: String): Boolean =
        dao.isWordSaved(word)

}