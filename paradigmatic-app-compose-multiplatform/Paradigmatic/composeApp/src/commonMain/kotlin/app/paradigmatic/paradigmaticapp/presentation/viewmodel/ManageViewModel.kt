package app.paradigmatic.paradigmaticapp.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import app.paradigmatic.paradigmaticapp.data.room.MemeDatabase
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import app.paradigmatic.paradigmaticapp.domain.model.Meme
import kotlinx.coroutines.launch


const val IMAGE_URL = "https://mezza9.app/placeholder.jpg"

class ManageViewModel(
    private val database: MemeDatabase,
    savedStateHandle: SavedStateHandle

): ViewModel() {
    val selectedBookId = 0
    var imageField = mutableStateOf(IMAGE_URL)
    var titleField = mutableStateOf("")
    var descriptionField = mutableStateOf("")
    var categoryField = mutableStateOf("")
    var tagsField = mutableStateOf("")
    var creatorField = mutableStateOf("")

    init {
        viewModelScope.launch {
            if (selectedBookId != -1) {
                val selectedBook = database.memeDao().getMemeById(selectedBookId)
                titleField.value = selectedBook.title
                descriptionField.value = selectedBook.description
                categoryField.value = selectedBook.category
                tagsField.value = selectedBook.tags
                creatorField.value = selectedBook.creator
            }
        }
    }

    fun insertMeme(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (
                    titleField.value.isNotEmpty() &&
                    descriptionField.value.isNotEmpty() &&
                    categoryField.value.isNotEmpty() &&
                    tagsField.value.isNotEmpty() &&
                    creatorField.value.isNotEmpty()
                ) {
                    database.memeDao()
                        .insertMeme(
                            meme = Meme(
                                image = imageField.value,
                                title = titleField.value,
                                description = descriptionField.value,
                                category = categoryField.value,
                                tags = tagsField.value,
                                creator = creatorField.value,
                                isFavorite = false
                            ),
                        )
                    onSuccess()
                } else {
                    onError("Fields cannot be empty")
                }
            } catch (e: Exception) {
                onError(e.toString())
            }
        }
    }

    fun updateMeme(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (
                    titleField.value.isNotEmpty() &&
                    descriptionField.value.isNotEmpty() &&
                    categoryField.value.isNotEmpty() &&
                    tagsField.value.isNotEmpty() &&
                    creatorField.value.isNotEmpty()
                    ) {
                    database.memeDao()
                        .updateMeme(
                            meme = Meme(
                                _id = selectedBookId,
                                image = IMAGE_URL,
                                title = titleField.value,
                                description = descriptionField.value,
                                category = categoryField.value,
                                tags = tagsField.value,
                                creator = creatorField.value,
                                isFavorite = database.memeDao()
                                    .getMemeById(selectedBookId).isFavorite
                            )
                        )
                    onSuccess()
                } else {
                    onError("Fields cannot be empty")
                }
            } catch (e: Exception) {
                onError(e.toString())
            }
        }
    }

}