package app.paradigmatic.paradigmaticapp.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun MainScreen() {
    var currentRoute by remember { mutableStateOf(BottomNavItem.Home.route) }

    Scaffold(
        bottomBar = {
            ParadigmaticBottomNavigation(
                currentRoute = currentRoute,
                onNavigate = { item ->
                    currentRoute = item.route
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentRoute) {
                BottomNavItem.Markets.route -> MarketsScreen()
                BottomNavItem.Trending.route -> TrendingScreen()
                BottomNavItem.Home.route -> HomeScreen()
                BottomNavItem.Bookmarks.route -> BookmarksScreen()
                BottomNavItem.More.route -> MoreScreen()
            }
        }
    }
}

