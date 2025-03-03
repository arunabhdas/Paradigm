package app.paradigmatic.paradigmaticapp.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import app.paradigmatic.paradigmaticapp.data.room.MemeDatabase
import app.paradigmatic.paradigmaticapp.domain.model.Meme
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val database: MemeDatabase,
    private val selectedMemeId: Int = 0
): ViewModel() {
    var selectedMeme: MutableState<Meme?> = mutableStateOf(null)
        private set
    var isFavorite = mutableStateOf(false)
        private set
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
                isFavorite.value = selectedMeme?.isFavorite ?: false
            }
        }
    }

    fun setFavoriteMeme() {
        viewModelScope.launch {
            if (selectedMemeId != -1) {
                database.memeDao()
                    .setFavoriteMeme(
                        memeId = selectedMemeId,
                        isFavorite = !isFavorite.value
                    )
            }
        }
    }

    fun deleteMeme() {
        viewModelScope.launch {
            database.memeDao()
                .deleteMemeById(selectedMemeId)
        }
    }
}