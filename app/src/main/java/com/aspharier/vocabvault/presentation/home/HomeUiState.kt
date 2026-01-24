package com.aspharier.vocabvault.presentation.home

import com.aspharier.vocabvault.data.remote.dto.WordResponseDto

data class HomeUiState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val wordResult: WordResponseDto? = null,
    val errorMessage: String? = null
)
