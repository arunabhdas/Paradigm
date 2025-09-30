package app.paradigmatic.paradigmaticapp.ui.navigation

sealed class ScreenDrawer {
    object HomeScreen: Screen(route = "home_screen")
    object MaintenanceRequestsScreen: Screen(route = "maintenance_requests_screen")
    object SettingsScreen: Screen(route = "settings_screen")
    object NotificationScreen: Screen(route = "notification_screen")
    object HomeDetailScreen: Screen(route = "home_detail_screen")
}

