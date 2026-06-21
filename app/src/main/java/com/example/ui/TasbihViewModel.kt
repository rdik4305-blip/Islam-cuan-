package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.db.TasbihDao
import com.example.db.TasbihSession
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TasbihViewModel(private val dao: TasbihDao) : ViewModel() {
    val session: StateFlow<TasbihSession?> = dao.getSession()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun increment() {
        viewModelScope.launch {
            val current = session.value ?: TasbihSession()
            val nextCount = if (current.count >= current.target) 0 else current.count + 1
            dao.saveSession(current.copy(count = nextCount))
        }
    }

    fun reset() {
        viewModelScope.launch {
            val current = session.value ?: TasbihSession()
            dao.saveSession(current.copy(count = 0))
        }
    }
}

class TasbihViewModelFactory(private val dao: TasbihDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TasbihViewModel(dao) as T
    }
}
