package app.paradigmatic.paradigmaticapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.paradigmatic.paradigmaticapp.ui.screens.HomeScreen
import app.paradigmatic.paradigmaticapp.ui.screens.sidedrawernav.NotificationScreen
import app.paradigmatic.paradigmaticapp.ui.screens.sidedrawernav.SettingsScreen
import app.paradigmatic.paradigmaticapp.ui.screens.sidedrawernav.MaintenanceRequestsScreen
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun SetupDrawerNavGraph(
    navController: NavHostController,
    navigator: DestinationsNavigator
) {
    NavHost(
        navController = navController,
        startDestination = ScreenDrawer.HomeScreen.route
        // TODO-FIXME-CLEANUP startDestination = Screen.MainScreen.route
    ) {

        composable(
            route = ScreenDrawer.HomeScreen.route
        ) {
            HomeScreen(
                navController = navController,
                navigator = navigator
            )
        }

        composable(
            route = ScreenDrawer.MaintenanceRequestsScreen.route
        ) {
            MaintenanceRequestsScreen(
                navController = navController,
                navigator = navigator
            )
        }

        composable(
            route = ScreenDrawer.SettingsScreen.route
        ) {
            SettingsScreen(
                navController = navController
            )
        }

        composable(
            route = ScreenDrawer.NotificationScreen.route
        ) {
            NotificationScreen(
                navController = navController,
                navigator = navigator
            )
        }

    }
}
/* TODO-FIXME-DEPRECATE
@Composable
fun SetupRootNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.LandingScreen.route
        // TODO-FIXME-CLEANUP startDestination = Screen.MainScreen.route
    ) {

        /*
        composable(
            route = Screen.LandingScreen.route
        ) {
            LandingScreen(
                navController = navController
            )
        }
        */

        composable(
            route = Screen.LoginScreen.route
        ) {
            LoginScreen(
                navController = navController
            )
        }

        composable(
            route = Screen.RegistrationScreen.route
        ) {
            RegistrationScreen(
                navController = navController
            )
        }

        composable(
            route = Screen.MainScreen.route
        ) {
            MainScreen(
                navController = navController
            )
        }
    }
}
*/