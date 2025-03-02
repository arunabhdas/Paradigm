package app.paradigmatic.paradigmaticapp.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import app.paradigmatic.paradigmaticapp.data.room.MemeDatabase
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


const val IMAGE_URL = "https://mezza9.app/placeholder.jpg"

class ManageViewModel(
    private val database: MemeDatabase,
    savedStateHandle: SavedStateHandle

): ViewModel() {
    val selectedBookId = 0
    var imageField = mutableStateOf(IMAGE_URL)
    var titleField = mutableStateOf("")
    var summaryField = mutableStateOf("")
    var categoryField = mutableStateOf("")
    var tagsField = mutableStateOf("")
    var creatorField = mutableStateOf("")

    init {
        viewModelScope.launch {
            if (selectedBookId != -1) {
                val selectedBook = database.memeDao().getMemeById(selectedBookId)
                titleField.value = selectedBook.title
                summaryField.value = selectedBook.description
                categoryField.value = selectedBook.category
                tagsField.value = selectedBook.tags.joinToString()
                creatorField.value = selectedBook.creator
            }
        }
    }
}