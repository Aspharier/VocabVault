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
import kotlinx.coroutines.flow.first
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
        WordDefinition(word = "Serendipity", phonetic = "/ˌsɛrənˈdɪpɪti/", partOfSpeech = "noun", definition = "The occurrence of events by chance in a happy or beneficial way."),
        WordDefinition(word = "Eloquent", phonetic = "/ˈɛləkwənt/", partOfSpeech = "adjective", definition = "Fluent or persuasive in speaking or writing."),
        WordDefinition(word = "Ubiquitous", phonetic = "/juːˈbɪkwɪtəs/", partOfSpeech = "adjective", definition = "Present, appearing, or found everywhere."),
        WordDefinition(word = "Capricious", phonetic = "/kəˈprɪʃəs/", partOfSpeech = "adjective", definition = "Given to sudden and unaccountable changes of mood or behavior."),
        WordDefinition(word = "Pernicious", phonetic = "/pərˈnɪʃəs/", partOfSpeech = "adjective", definition = "Having a harmful effect, especially in a gradual or subtle way."),
        WordDefinition(word = "Pragmatic", phonetic = "/præɡˈmætɪk/", partOfSpeech = "adjective", definition = "Dealing with things sensibly and realistically in a practical way."),
        WordDefinition(word = "Ephemeral", phonetic = "/ɪˈfɛmərəl/", partOfSpeech = "adjective", definition = "Lasting for a very short time; transient."),
        WordDefinition(word = "Luminous", phonetic = "/ˈluːmɪnəs/", partOfSpeech = "adjective", definition = "Bright or shining, especially in the dark."),
        WordDefinition(word = "Nefarious", phonetic = "/nɪˈfɛəriəs/", partOfSpeech = "adjective", definition = "Wicked or criminal."),
        WordDefinition(word = "Solitude", phonetic = "/ˈsɒlɪtjuːd/", partOfSpeech = "noun", definition = "The state or situation of being alone."),
        WordDefinition(word = "Melancholy", phonetic = "/ˈmɛlənkɒli/", partOfSpeech = "noun", definition = "A feeling of pensive sadness, typically with no obvious cause."),
        WordDefinition(word = "Halcyon", phonetic = "/ˈhælsiən/", partOfSpeech = "adjective", definition = "Denoting a period of time in the past that was idyllically happy and peaceful."),
        WordDefinition(word = "Ethereal", phonetic = "/ɪˈθɪəriəl/", partOfSpeech = "adjective", definition = "Extremely delicate and light in a way that seems too perfect for this world."),
        WordDefinition(word = "Limerence", phonetic = "/ˈlɪmərəns/", partOfSpeech = "noun", definition = "The state of being infatuated or obsessed with another person."),
        WordDefinition(word = "Petrichor", phonetic = "/ˈpɛtrʌɪkɔː/", partOfSpeech = "noun", definition = "A pleasant smell that frequently accompanies the first rain after a warm period."),
        WordDefinition(word = "Somnambulist", phonetic = "/sɒmˈnæmbjʊlɪst/", partOfSpeech = "noun", definition = "A person who sleepwalks."),
        WordDefinition(word = "Vellichor", phonetic = "/ˈvɛlɪkɔː/", partOfSpeech = "noun", definition = "The strange wistfulness of used bookstores and their unique history."),
        WordDefinition(word = "Defenestration", phonetic = "/ˌdiːfɛnɪˈstreɪʃn/", partOfSpeech = "noun", definition = "The action of throwing someone out of a window."),
        WordDefinition(word = "Sonder", phonetic = "/ˈsɒndə/", partOfSpeech = "noun", definition = "The realization that each random passerby is living a complex life like your own."),
        WordDefinition(word = "Effervescence", phonetic = "/ˌɛfəˈvɛsns/", partOfSpeech = "noun", definition = "Bubbling, fizzing, or high vivacity and enthusiasm."),
        WordDefinition(word = "Epoch", phonetic = "/ˈiːpɒk/", partOfSpeech = "noun", definition = "A particular period of time in history or a person's life."),
        WordDefinition(word = "Panacea", phonetic = "/ˌpænəˈsiːə/", partOfSpeech = "noun", definition = "A solution or remedy for all difficulties or diseases."),
        WordDefinition(word = "Mellifluous", phonetic = "/mɪˈlɪflʊəs/", partOfSpeech = "adjective", definition = "Sweet or musical; pleasant to hear."),
        WordDefinition(word = "Obsequious", phonetic = "/əbˈsiːkwiəs/", partOfSpeech = "adjective", definition = "Obedient or attentive to an excessive or servile degree."),
        WordDefinition(word = "Alacrity", phonetic = "/əˈlækrɪti/", partOfSpeech = "noun", definition = "Brisk and cheerful readiness."),
        WordDefinition(word = "Cacophony", phonetic = "/kəˈkɒfəni/", partOfSpeech = "noun", definition = "A harsh, discordant mixture of sounds."),
        WordDefinition(word = "Epiphany", phonetic = "/ɪˈpɪfəni/", partOfSpeech = "noun", definition = "A moment of sudden and great revelation or realization."),
        WordDefinition(word = "Paradigm", phonetic = "/ˈpærədaɪm/", partOfSpeech = "noun", definition = "A typical example or pattern of something; a model."),
        WordDefinition(word = "Sycophant", phonetic = "/ˈsɪkəfænt/", partOfSpeech = "noun", definition = "A person who acts obsequiously toward someone important to gain advantage."),
        WordDefinition(word = "Surreptitious", phonetic = "/ˌsʌrəpˈtɪʃəs/", partOfSpeech = "adjective", definition = "Kept secret, especially because it would not be approved of.")
    )

    init {
        observeDashboard()

        // Record active session day on launch
        viewModelScope.launch {
            preferences.recordActivityAndIncrementStreak()
        }
    }

    private fun getStartOfDayTimestamp(): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private data class TripleStats(
        val due: Int,
        val total: Int,
        val streak: Int,
        val xp: Int
    )

    private fun observeDashboard() {
        viewModelScope.launch {
            val dueCountFlow = repository.getWordsDueForReviewCount(System.currentTimeMillis())
            val totalCountFlow = repository.getTotalWordsCount()
            val streakFlow = preferences.getStreak()
            val xpFlow = preferences.getXp()
            val savedWordsFlow = repository.getSavedWords()
            val challengeCompletedFlow = preferences.getChallengeCompletedToday()

            val statsFlow = combine(dueCountFlow, totalCountFlow, streakFlow, xpFlow) { due, total, streak, xp ->
                TripleStats(due, total, streak, xp)
            }

            combine(
                statsFlow,
                savedWordsFlow,
                challengeCompletedFlow
            ) { stats, savedList, challengeCompleted ->
                val due = stats.due
                val total = stats.total
                val streak = stats.streak
                val xp = stats.xp

                val savedToday = savedList.count { it.savedAt >= getStartOfDayTimestamp() }
                val progress = (savedToday / 3.0f).coerceAtMost(1.0f)
                val statusText = when {
                    challengeCompleted || savedToday >= 3 -> {
                        "Mission accomplished! You saved $savedToday words today. 🔥 (+15 XP awarded)"
                    }
                    savedToday == 0 -> {
                        "0 of 3 words saved. Save 3 new words to complete today's challenge!"
                    }
                    else -> {
                        "$savedToday of 3 words saved. Save ${3 - savedToday} more words to complete today's challenge!"
                    }
                }

                val savedWordsSet = savedList.map { it.word.lowercase() }.toSet()

                // Calculate Word of the Day (rotate based on day of month)
                val dayIndex = (SimpleDateFormat("d", Locale.getDefault()).format(Date()).toIntOrNull() ?: 1) % staticWords.size
                val rawWotd = staticWords[dayIndex]
                
                // Recommendations: take 3 static words that are NOT the WOTD and NOT already saved in the database
                val unsavedRecs = staticWords
                    .filterNot { it.word.lowercase() == rawWotd.word.lowercase() }
                    .filterNot { savedWordsSet.contains(it.word.lowercase()) }
                    .shuffled()
                    .take(3)
                
                // Fallback: if we don't have 3 unsaved recommendations, fill with remaining static words
                val recs = if (unsavedRecs.size < 3) {
                    val remaining = staticWords
                        .filterNot { it.word.lowercase() == rawWotd.word.lowercase() }
                        .filterNot { w -> unsavedRecs.any { it.word.lowercase() == w.word.lowercase() } }
                        .shuffled()
                    unsavedRecs + remaining.take(3 - unsavedRecs.size)
                } else {
                    unsavedRecs
                }

                _uiState.update {
                    it.copy(
                        wordsDueCount = due,
                        totalWordsCount = total,
                        streakCount = streak,
                        xpCount = xp,
                        wordOfTheDay = rawWotd,
                        recommendations = recs,
                        challengeProgress = progress,
                        challengeStatus = statusText
                    )
                }
            }.collectLatest {}
        }
    }

    fun saveWord(word: WordDefinition) {
        viewModelScope.launch {
            if (repository.isWordSaved(word.word)) {
                _eventFlow.emit(HomeUiEvent.ShowSnackbar("Already saved in your vault"))
            } else {
                repository.saveWord(word)
                preferences.addXp(10)
                preferences.recordActivityAndIncrementStreak()

                val startOfDay = getStartOfDayTimestamp()
                val savedWords = repository.getSavedWords().first()
                val savedToday = savedWords.count { it.savedAt >= startOfDay }

                if (savedToday >= 3) {
                    val awarded = preferences.checkAndAwardChallengeBonus()
                    if (awarded) {
                        _eventFlow.emit(HomeUiEvent.ShowSnackbar("${word.word} saved (+10 XP)! Daily Challenge Complete (+15 XP)!"))
                    } else {
                        _eventFlow.emit(HomeUiEvent.ShowSnackbar("${word.word} saved (+10 XP)!"))
                    }
                } else {
                    _eventFlow.emit(HomeUiEvent.ShowSnackbar("${word.word} saved (+10 XP)!"))
                }
            }
        }
    }
}
