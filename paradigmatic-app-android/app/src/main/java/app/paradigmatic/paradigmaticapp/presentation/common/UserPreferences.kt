package app.paradigmatic.paradigmaticapp.presentation.common

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey


object UserPreferences {
    val userId = intPreferencesKey("userId")
    val username = stringPreferencesKey("username")
    val email = stringPreferencesKey("email")
    val password = stringPreferencesKey("password")
    val accessToken = stringPreferencesKey("accessToken")
    val refreshToken = stringPreferencesKey("refreshToken")
    var isUserLoggedIn = booleanPreferencesKey("isUserLoggedIn")
    var isOnboardingShown = booleanPreferencesKey("isOnBoardingShown")
    var showLocationPermissionDialog = booleanPreferencesKey("showLocationPermissionDialog")
}