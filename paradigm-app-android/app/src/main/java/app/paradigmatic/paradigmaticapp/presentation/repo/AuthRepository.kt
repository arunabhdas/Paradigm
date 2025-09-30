package app.paradigmatic.paradigmaticapp.presentation.repo

import app.paradigmatic.paradigmaticapp.presentation.common.UserPreferences
import app.paradigmatic.paradigmaticapp.presentation.service.ApiService
import app.paradigmatic.paradigmaticapp.presentation.data.model.LoginRequest
import app.paradigmatic.paradigmaticapp.presentation.data.model.LoginResponse
import app.paradigmatic.paradigmaticapp.presentation.data.model.RegisterRequest
import app.paradigmatic.paradigmaticapp.presentation.data.model.RegisterResponse
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@ViewModelScoped
class AuthRepository @Inject internal constructor(
    private val apiService: ApiService,
    private val preferencesDataStore: DataStore<Preferences>
){
    suspend fun loginUser(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.loginUser(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error logging in"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerUser(email: String, username: String, password: String): Result<RegisterResponse> {
        return try {
            val response = apiService.registerUser(RegisterRequest(email, username, password))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error logging in"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveUserIdToDataStore(userId: Int) {
        preferencesDataStore.edit {preference ->
            preference[UserPreferences.userId] = userId
        }
    }

    val readUserIdFromDataStore: Flow<Int> = preferencesDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.d(exception.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {preferences ->
            preferences[UserPreferences.userId] ?: 0
        }

    suspend fun saveUsernameToDataStore(username: String) {
        preferencesDataStore.edit { preference ->
            preference[UserPreferences.username] = username
        }
    }

    val readUsernameFromDataStore: Flow<String> = preferencesDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.d(exception.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[UserPreferences.username] ?: ""
        }


    suspend fun saveEmailToDataStore(email: String) {
        preferencesDataStore.edit { preference ->
            preference[UserPreferences.email] = email
        }
    }

    val readEmailFromDataStore: Flow<String> = preferencesDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.d(exception.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[UserPreferences.email] ?: ""
        }


    suspend fun savePasswordToDataStore(password: String) {
        preferencesDataStore.edit { preference ->
            preference[UserPreferences.password] = password
        }
    }

    val readPasswordFromDataStore: Flow<String> = preferencesDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.d(exception.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[UserPreferences.password] ?: ""
        }




    suspend fun saveAccessTokenToDataStore(accessToken: String) {
        preferencesDataStore.edit { preference ->
            preference[UserPreferences.accessToken] = accessToken
        }
    }

    val readAccessTokenFromDataStore: Flow<String> = preferencesDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.d(exception.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[UserPreferences.accessToken] ?: ""
        }

    suspend fun saveRefreshTokenToDataStore(refreshToken: String) {
        preferencesDataStore.edit { preference ->
            preference[UserPreferences.refreshToken] = refreshToken
        }
    }

    val readRefreshTokenFromDataStore: Flow<String> = preferencesDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.d(exception.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[UserPreferences.refreshToken] ?: ""
        }


    val readIsOnboardingShownFromDataStore: Flow<Boolean> = preferencesDataStore.data
        .catch { exception ->
            if(exception is IOException) {
                Timber.d(exception.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[UserPreferences.isOnboardingShown] ?: false
        }

    suspend fun saveIsOnboardingShowToDataStore(isOnboardingShown: Boolean) {
        preferencesDataStore.edit { preference ->
            preference[UserPreferences.isOnboardingShown] = isOnboardingShown
        }
    }

    val readIsUserLoggedInFromDataStore: Flow<Boolean> = preferencesDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.d(exception.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[UserPreferences.isUserLoggedIn] ?: false
        }

    suspend fun saveIsUserLoggedIn(isUserLoggedIn: Boolean) {
        preferencesDataStore.edit { preference ->
            preference[UserPreferences.isUserLoggedIn] = isUserLoggedIn
        }
    }


}