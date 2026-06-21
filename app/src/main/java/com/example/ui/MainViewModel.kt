package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.AladhanApi
import com.example.api.AladhanResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val response: AladhanResponse) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class MainViewModel : ViewModel() {
    private val aladhanApi = AladhanApi.create()
    
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _city = MutableStateFlow("Jakarta")
    val city: StateFlow<String> = _city.asStateFlow()

    private val _country = MutableStateFlow("Indonesia")
    val country: StateFlow<String> = _country.asStateFlow()

    init {
        fetchTimings()
    }

    fun setLocation(newCity: String, newCountry: String) {
        _city.value = newCity
        _country.value = newCountry
        fetchTimings()
    }

    fun fetchTimings() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val data = aladhanApi.getTimings(_city.value, _country.value)
                _uiState.value = HomeUiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
