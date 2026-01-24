package com.aspharier.vocabvault.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspharier.vocabvault.data.remote.api.DictionaryApi
import com.aspharier.vocabvault.data.remote.dto.WordResponseDto
import com.aspharier.vocabvault.domain.model.WordDefinition
import com.aspharier.vocabvault.domain.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: DictionaryApi,
    private val repository: WordRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private val _eventFlow = MutableSharedFlow<HomeUiEvent>()
    val eventFlow = _eventFlow

    fun onSearchQueryChange(query: String) {
        _uiState.update {
            it.copy(searchQuery = query)
        }
    }

    fun onSearchClicked() {
        // API call
        val word = uiState.value.searchQuery.trim()
        if(word.isEmpty()) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }
            try {
                val dto = api.getWordDefinition(word).first()
                val domainWord = mapToDomain(dto)
                val saved = repository.isWordSaved(domainWord.word)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        wordResult = domainWord,
                        isSaved = saved
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Word not found"
                    )
                }
            }
        }
    }

    fun onSavedClicked() {
        val word = uiState.value.wordResult ?: return

        viewModelScope.launch {
            if(repository.isWordSaved(word.word)) {
                _eventFlow.emit(
                    HomeUiEvent.ShowSnackbar("Already saved")
                )
            } else {
                repository.saveWord(word)
                _uiState.update { it.copy(isSaved = true) }
                _eventFlow.emit(
                    HomeUiEvent.ShowSnackbar("Word saved successfully")
                )
            }
        }
    }

    private fun mapToDomain(dto: WordResponseDto): WordDefinition {
        val meaning = dto.meanings.first()
        val definition = meaning.definitions.first()

        return WordDefinition(
            word = dto.word,
            phonetic = dto.phonetic,
            partOfSpeech = meaning.partOfSpeech,
            definition = definition.definition
        )
    }

}