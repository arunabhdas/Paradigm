package app.paradigmatic.paradigmaticapp.ui.screens.sidedrawernav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.paradigmatic.paradigmaticapp.ui.navigation.CustomAppBar
import app.paradigmatic.paradigmaticapp.ui.navigation.MenuItem
import app.paradigmatic.paradigmaticapp.ui.navigation.SetupDrawerNavGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import timber.log.Timber

@Destination
@Composable
fun MainScreen(
    navController: NavController,
    navigator: DestinationsNavigator,
    titles: List<String> = listOf("XLR8R")
) {
    var drawerNavHostController: NavHostController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            DrawerBody(
                items = listOf(
                    MenuItem(
                        id = "home",
                        title = "Home",
                        route = "home_screen",
                        contentDescription = "Navigate to Home",
                        icon = Icons.Default.Home
                    ),
                    MenuItem(
                        id = "maintenance_requests",
                        title = "Workorders",
                        route = "maintenance_requests_screen",
                        contentDescription = "Navigate to Maintenance Requests",
                        icon = Icons.Default.Settings
                    ),
                    MenuItem(
                        id = "settings",
                        title = "Settings",
                        route = "settings_screen",
                        contentDescription = "Navigate to Settings",
                        icon = Icons.Default.Settings
                    ),
                    MenuItem(
                        id = "notifications",
                        title = "Notifications",
                        route = "notification_screen",
                        contentDescription = "Navigate to Notifications",
                        icon = Icons.Default.Notifications
                    )
                ),
                onItemClick = { menuItem ->
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                    Timber.d( "Clicked on ${menuItem.title}")
                    // TODO-FIXME navController.navigate(route = menuItem.route)
                    // navigator.navigate(route = menuItem.route)
                    drawerNavHostController.navigate(route = menuItem.route)
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            SetupDrawerNavGraph(
                navController = drawerNavHostController as NavHostController,
                navigator = navigator
            )
            CustomAppBar(
                onNavigationIconClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            )
        }
    }
}

@Composable
@Preview
fun MainScreenPreview() {
    // BottomNavigationMainScreen("Main", rememberNavController())
}