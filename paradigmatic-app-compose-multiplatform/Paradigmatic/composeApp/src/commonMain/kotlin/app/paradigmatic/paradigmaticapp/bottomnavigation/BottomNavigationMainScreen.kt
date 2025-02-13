package app.paradigmatic.paradigmaticapp.bottomnavigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import app.paradigmatic.paradigmaticapp.navigation.*
import cafe.adriel.voyager.core.screen.Screen

class BottomNavigationMainScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        var selectedIndex by remember { mutableStateOf(0) }
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Paradigmatic") },
                    navigationIcon = {
                        IconButton(onClick = { /* Handle navigation */ }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Handle search */ }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                        IconButton(onClick = { /* Handle more options */ }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options"
                            )
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    listOf(
                        BottomNavItem.Markets,
                        BottomNavItem.Trending,
                        BottomNavItem.Home,
                        BottomNavItem.Bookmarks,
                        BottomNavItem.More
                    ).forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index }
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (selectedIndex) {
                    0 -> MarketsScreen()
                    1 -> TrendingScreen()
                    2 -> HomeScreen()
                    3 -> BookmarksScreen()
                    4 -> MoreScreen()
                }
            }
        }
    }
}
