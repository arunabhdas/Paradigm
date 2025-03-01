package app.paradigmatic.paradigmaticapp.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import app.paradigmatic.paradigmaticapp.data.room.MemeDatabase
import app.paradigmatic.paradigmaticapp.domain.model.Meme
import app.paradigmatic.paradigmaticapp.domain.model.RequestState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf

class MemeViewModel (
    private val database: MemeDatabase
): ViewModel() {

    private var _sortedByFavorite = MutableStateFlow(false)
    val sortedByFavorite: StateFlow<Boolean> = _sortedByFavorite

    private var _memes: MutableState<RequestState<List<Meme>>> = mutableStateOf(RequestState.Loading)
    val memes: State<RequestState<List<Meme>>> = _memes

    init {
        viewModelScope.launch {
            database.memeDao().readAllMemes().collectLatest {
                _memes.value = RequestState.Success(
                    data = it
                )
            }
        }
    }

    fun toggleSortByFavorite() {
        _sortedByFavorite.value = !_sortedByFavorite.value
    }
}