package app.paradigmatic.paradigmaticapp.ui.screens.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun LoadingUI() {
    CircularProgressIndicator() // Material Design loading spinner
}

@Composable
fun ErrorUI(errorMessage: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Error: $errorMessage", color = MaterialTheme.colorScheme.error)
        Button(onClick = { /* Retry or navigate back */ }) {
            Text("Retry")
        }
    }
}

@Composable
fun IdleUI() {
    Text(text = "Please log in", style = MaterialTheme.typography.titleSmall)
}

