package app.paradigmatic.paradigmaticapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Markets : BottomNavItem(
        route = "markets",
        title = "Markets",
        icon = Icons.Default.Menu
    )
    
    data object Trending : BottomNavItem(
        route = "trending",
        title = "Trending",
        icon = Icons.Default.Create
    )
    
    data object Home : BottomNavItem(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    
    data object Bookmarks : BottomNavItem(
        route = "bookmarks",
        title = "Bookmarks",
        icon = Icons.Default.Favorite
    )
    
    data object More : BottomNavItem(
        route = "more",
        title = "More",
        icon = Icons.Default.MoreVert
    )
    
    companion object {
        val items = listOf(
            Markets,
            Trending,
            Home,
            Bookmarks,
            More
        )
    }
}