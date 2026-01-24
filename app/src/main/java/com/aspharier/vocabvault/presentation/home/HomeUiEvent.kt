package com.aspharier.vocabvault.presentation.home

sealed class HomeUiEvent {
    data class ShowSnackbar(val message: String) : HomeUiEvent()
}