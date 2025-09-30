package app.paradigmatic.paradigmaticapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel


class SharedNavigationViewModel : ViewModel() {
    var showDetailScreen by mutableStateOf(false)
        private set

    fun toggleDetailScreen(show: Boolean) {
        showDetailScreen = show
    }
}