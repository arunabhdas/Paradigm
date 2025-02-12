package app.paradigmatic.paradigmaticapp.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import app.paradigmatic.paradigmaticapp.navigation.BottomNavItem

@Composable
fun ParadigmaticBottomNavigation(
    currentRoute: String,
    onNavigate: (BottomNavItem) -> Unit
) {
    NavigationBar {
        listOf(
            BottomNavItem.Markets,
            BottomNavItem.Trending,
            BottomNavItem.Home,
            BottomNavItem.Bookmarks,
            BottomNavItem.More
        ).forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = { onNavigate(item) }
            )
        }
    }
}