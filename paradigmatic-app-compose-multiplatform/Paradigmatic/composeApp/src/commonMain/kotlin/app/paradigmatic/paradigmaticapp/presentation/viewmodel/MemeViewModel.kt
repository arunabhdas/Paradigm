package app.paradigmatic.paradigmaticapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import app.paradigmatic.paradigmaticapp.data.room.MemeDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MemeViewModel (
    private val database: MemeDatabase
): ViewModel() {

    private var _sortedByFavorite = MutableStateFlow(false)
    val sortedByFavorite: StateFlow<Boolean> = _sortedByFavorite

    init {
        viewModelScope.launch {
            database.memeDao().readAllMemes().collectLatest {

            }
        }
    }
}