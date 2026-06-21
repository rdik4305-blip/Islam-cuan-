package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.QuranApi
import com.example.api.Surah
import com.example.api.SurahDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class QuranUiState {
    object Loading : QuranUiState()
    data class SurahList(val surahs: List<Surah>) : QuranUiState()
    data class SurahDetail(val details: SurahDetails) : QuranUiState()
    data class Error(val message: String) : QuranUiState()
}

class QuranViewModel : ViewModel() {
    private val quranApi = QuranApi.create()

    private val _uiState = MutableStateFlow<QuranUiState>(QuranUiState.Loading)
    val uiState: StateFlow<QuranUiState> = _uiState.asStateFlow()

    init {
        loadSurahs()
    }

    fun loadSurahs() {
        viewModelScope.launch {
            _uiState.value = QuranUiState.Loading
            try {
                val response = quranApi.getSurahs()
                _uiState.value = QuranUiState.SurahList(response.data)
            } catch (e: Exception) {
                _uiState.value = QuranUiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun loadSurahDetail(number: Int) {
        viewModelScope.launch {
            _uiState.value = QuranUiState.Loading
            try {
                val response = quranApi.getSurahDetails(number)
                _uiState.value = QuranUiState.SurahDetail(response.data)
            } catch (e: Exception) {
                _uiState.value = QuranUiState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}
