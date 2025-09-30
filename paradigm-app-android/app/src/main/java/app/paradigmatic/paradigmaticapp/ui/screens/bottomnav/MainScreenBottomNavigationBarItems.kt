package app.paradigmatic.paradigmaticapp.ui.screens.bottomnav

import app.paradigmatic.paradigmaticapp.R


enum class MainScreenBottomNavigationBarItems(val icon: Int){
    One(icon = R.drawable.map_24px),
    Two(icon = R.drawable.location_on_24px),
    Three(icon = R.drawable.appicon),
    Four(icon = R.drawable.explore_24px),
    Five(icon = R.drawable.setting)
}

/* TODO-FIXME-CLEANUP
enum class NavigationBarItems(val icon: Int){
    Report(icon = R.drawable.rounded_rect),
    Notification(icon = R.drawable.outline_bell),
    Home(icon = R.drawable.appicon),
    Record(icon = R.drawable.calendar),
    Help(icon = R.drawable.gear)
}
*/