package com.aspharier.vocabvault.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspharier.vocabvault.data.local.ThemePreferences
import com.aspharier.vocabvault.domain.model.WordDefinition
import com.aspharier.vocabvault.domain.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WordRepository,
    private val preferences: ThemePreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<HomeUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val staticWords = listOf(
        WordDefinition(
            word = "Serendipity",
            phonetic = "/ˌsɛrənˈdɪpɪti/",
            partOfSpeech = "noun",
            definition = "The occurrence of events by chance in a happy or beneficial way."
        ),
        WordDefinition(
            word = "Eloquent",
            phonetic = "/ˈɛləkwənt/",
            partOfSpeech = "adjective",
            definition = "Fluent or persuasive in speaking or writing."
        ),
        WordDefinition(
            word = "Ubiquitous",
            phonetic = "/juːˈbɪkwɪtəs/",
            partOfSpeech = "adjective",
            definition = "Present, appearing, or found everywhere."
        ),
        WordDefinition(
            word = "Capricious",
            phonetic = "/kəˈprɪʃəs/",
            partOfSpeech = "adjective",
            definition = "Given to sudden and unaccountable changes of mood or behavior."
        ),
        WordDefinition(
            word = "Pernicious",
            phonetic = "/pərˈnɪʃəs/",
            partOfSpeech = "adjective",
            definition = "Having a harmful effect, especially in a gradual or subtle way."
        ),
        WordDefinition(
            word = "Pragmatic",
            phonetic = "/præɡˈmætɪk/",
            partOfSpeech = "adjective",
            definition = "Dealing with things sensibly and realistically in a practical way."
        )
    )

    init {
        // Set Word of the Day based on day of month
        val dayIndex = (SimpleDateFormat("d", Locale.getDefault()).format(Date()).toIntOrNull() ?: 1) % staticWords.size
        val wotd = staticWords[dayIndex]
        val recs = staticWords.filterNot { it.word == wotd.word }

        _uiState.update {
            it.copy(
                wordOfTheDay = wotd,
                recommendations = recs
            )
        }

        observeCounts()

        // Record active session day on launch
        viewModelScope.launch {
            preferences.recordActivityAndIncrementStreak()
        }
    }

    private fun observeCounts() {
        viewModelScope.launch {
            val dueCountFlow = repository.getWordsDueForReviewCount(System.currentTimeMillis())
            val totalCountFlow = repository.getTotalWordsCount()
            val streakFlow = preferences.getStreak()

            combine(dueCountFlow, totalCountFlow, streakFlow) { due, total, streak ->
                Triple(due, total, streak)
            }.collectLatest { (due, total, streak) ->
                _uiState.update {
                    it.copy(
                        wordsDueCount = due,
                        totalWordsCount = total,
                        streakCount = streak
                    )
                }
            }
        }
    }

    fun saveWord(word: WordDefinition) {
        viewModelScope.launch {
            if (repository.isWordSaved(word.word)) {
                _eventFlow.emit(HomeUiEvent.ShowSnackbar("Already saved in your vault"))
            } else {
                repository.saveWord(word)
                preferences.recordActivityAndIncrementStreak()
                _eventFlow.emit(HomeUiEvent.ShowSnackbar("${word.word} saved!"))
            }
        }
    }
}
