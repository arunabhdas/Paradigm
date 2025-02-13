package app.paradigmatic.paradigmaticapp.bottomnavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomNavigationItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badges: Int
) {
    data object TabOneItem: BottomNavigationItem(
        title = "Markets",
        route = "markets",
        selectedIcon = Icons.Default.Menu,
        unselectedIcon = Icons.Outlined.Menu,
        hasNews = false,
        badges = 0
    )

    data object TabTwoItem: BottomNavigationItem(
        title = "Trending",
        route = "trending",
        selectedIcon = Icons.Default.Create,
        unselectedIcon = Icons.Outlined.Create,
        hasNews = true,
        badges = 0
    )

    data object TabThreeItem: BottomNavigationItem(
        title = "Home",
        route = "home",
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Outlined.Home,
        hasNews = false,
        badges = 0
    )

    data object TabFourItem: BottomNavigationItem(
        title = "Alerts",
        route = "alerts",
        selectedIcon = Icons.Default.Notifications,
        unselectedIcon = Icons.Outlined.Notifications,
        hasNews = false,
        badges = 0
    )

    data object TabFiveItem: BottomNavigationItem(
        title = "More",
        route = "more",
        selectedIcon = Icons.Default.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle,
        hasNews = false,
        badges = 0
    )
}
/* TODO-FIXME
val bottomNavigationItems = listOf(
    BottomNavigationItem(
        title = "Favorites",
        route = "favorites",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.Favorite,
        hasNews = false,
        badges = 0
    ),
    BottomNavigationItem(
        title = "Portfolio",
        route = "portfolio",
        selectedIcon = Icons.Filled.Build,
        unselectedIcon = Icons.Outlined.Build,
        hasNews = true,
        badges = 0
    ),
    BottomNavigationItem(
        title = "Home",
        route = "home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        hasNews = false,
        badges = 0
    ),
    BottomNavigationItem(
        title = "Alerts",
        route = "alerts",
        selectedIcon = Icons.Filled.Notifications,
        unselectedIcon = Icons.Outlined.Notifications,
        hasNews = false,
        badges = 0
    ),
    BottomNavigationItem(
        title = "Profile",
        route = "profile",
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle,
        hasNews = false,
        badges = 0
    )
)
*/