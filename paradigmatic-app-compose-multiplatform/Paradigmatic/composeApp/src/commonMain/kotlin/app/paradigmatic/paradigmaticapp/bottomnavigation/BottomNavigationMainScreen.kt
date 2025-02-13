package app.paradigmatic.paradigmaticapp.bottomnavigation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import app.paradigmatic.paradigmaticapp.bottomnavigation.bottomNavigationItems

@Composable
fun BottomNavigationMainScreen(
) {
    var selectedRoute by remember {
        mutableStateOf(0)
    }
    Scaffold (
        bottomBar = {
            NavigationBar {
                bottomNavigationItems.forEachIndexed { index, bottomNavigationItem ->
                    NavigationBarItem(
                        selected = index == selectedRoute,
                        onClick = {
                            selectedRoute = index

                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (bottomNavigationItem.badges != 0) {
                                        Badge {
                                            Text(text = bottomNavigationItem.badges.toString())
                                        }
                                    } else if (bottomNavigationItem.hasNews) {
                                        Badge()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector =
                                        if (index == selectedRoute) {
                                            bottomNavigationItem.selectedIcon
                                        } else {
                                            bottomNavigationItem.unselectedIcon
                                        },
                                        contentDescription = bottomNavigationItem.title
                                    )
                            }
                        },
                        label = {
                            Text(text = bottomNavigationItem.title)

                        }
                    )

                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO */ }) {

            }
        }
    ) { paddingValues ->
        val padding = paddingValues
    }
    /* TODO-FIXME-CLEANUP-PRIOR-TO-REFACTORING
    var currentRoute by remember { mutableStateOf(BottomNavItem.Home.route) }

    Scaffold(
        bottomBar = {
            BottomNav(
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
    */
}

