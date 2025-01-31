package app.paradigmatic.paradigmaticapp.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val id: String,
    val title: String,
    val route: String,
    val contentDescription: String,
    val icon: ImageVector
)