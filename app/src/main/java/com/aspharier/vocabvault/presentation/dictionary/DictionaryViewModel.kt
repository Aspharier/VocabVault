package com.aspharier.vocabvault.presentation.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspharier.vocabvault.domain.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private val repository: WordRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DictionaryUiState())
    val uiState: StateFlow<DictionaryUiState> = _uiState

    init {
        observeSavedWords()
    }

    private fun observeSavedWords() {
        viewModelScope.launch {
            repository.getSavedWords().collectLatest { words ->
                _uiState.value = DictionaryUiState(words)
            }
        }
    }

    fun deleteWord(word: String) {
        viewModelScope.launch {
            repository.deleteWord(word)
        }
    }
}