package app.paradigmatic.paradigmaticapp.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomNav(
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
                icon = {
                    Icon(
                    imageVector = item.icon,
                    contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = { onNavigate(item) }
            )
        }
    }
}