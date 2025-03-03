package app.paradigmatic.paradigmaticapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import app.paradigmatic.paradigmaticapp.data.room.MemeDatabase
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import app.paradigmatic.paradigmaticapp.domain.model.Meme
import kotlinx.coroutines.launch


const val IMAGE_URL = "https://mezza9.app/placeholder.jpg"

class ManageViewModel(
    private val database: MemeDatabase,
    private val selectedMemeId: Int

): ViewModel() {
    var imageField = mutableStateOf(IMAGE_URL)
    var titleField = mutableStateOf("")
    var descriptionField = mutableStateOf("")
    var categoryField = mutableStateOf("")
    var tagsField = mutableStateOf("")
    var creatorField = mutableStateOf("")

    init {
        viewModelScope.launch {
            if (selectedMemeId != -1) {
                val selectedMeme = database.memeDao().getMemeById(selectedMemeId)
                titleField.value = selectedMeme?.title ?: ""
                descriptionField.value = selectedMeme?.description ?: ""
                categoryField.value = selectedMeme?.category ?: ""
                tagsField.value = selectedMeme?.tags ?: ""
                creatorField.value = selectedMeme?.creator ?: ""
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
                                _id = selectedMemeId,
                                image = IMAGE_URL,
                                title = titleField.value,
                                description = descriptionField.value,
                                category = categoryField.value,
                                tags = tagsField.value,
                                creator = creatorField.value,
                                isFavorite = database.memeDao()
                                    .getMemeById(selectedMemeId)?.isFavorite ?: false
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