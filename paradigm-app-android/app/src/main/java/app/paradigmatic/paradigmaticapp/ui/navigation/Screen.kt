package app.paradigmatic.paradigmaticapp.ui.navigation

sealed class Screen(val route: String) {
    object LandingScreen: Screen(route = "landing_screen")
    object LoginScreen: Screen(route = "login_screen")
    object RegistrationScreen: Screen(route = "registration_screen")
    object ScannerScreen: Screen(route = "scanner_screen")
    object TestScreen: Screen(route = "test_screen")
    object ScannerView: Screen(route = "scanner_view")
    object ConnectScreen: Screen(route = "connect_screen")
    object MainScreen: Screen(route = "main_screen")
    object SettingsScreen: Screen(route = "settings_screen")
    object NotificationScreen: Screen(route = "notification_screen")
    object HomeDetailScreen: Screen(route = "home_detail_screen")
    object PermissionsScreen: Screen(route = "permissions_screen")
}