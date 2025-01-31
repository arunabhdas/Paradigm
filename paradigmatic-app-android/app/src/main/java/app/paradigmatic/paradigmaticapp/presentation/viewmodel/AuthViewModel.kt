package app.paradigmatic.paradigmaticapp.presentation.viewmodel

import app.paradigmatic.paradigmaticapp.data.supabase.SupabaseClient
import app.paradigmatic.paradigmaticapp.presentation.data.model.LoginResponseSupabase
import app.paradigmatic.paradigmaticapp.presentation.data.model.LoginResponse
import app.paradigmatic.paradigmaticapp.presentation.data.model.RegisterResponse
import app.paradigmatic.paradigmaticapp.presentation.repo.AuthRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
open class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : AndroidViewModel(Application()) {

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    private val _loginSupabaseUiState =
        MutableStateFlow<LoginSupabaseUiState>(LoginSupabaseUiState.Idle)
    val loginSupabaseUiState: StateFlow<LoginSupabaseUiState> = _loginSupabaseUiState.asStateFlow()


    private val _registerUiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val registerUiState: StateFlow<RegisterUiState> = _registerUiState.asStateFlow()

    private val _userId = MutableStateFlow(0)
    val userId: StateFlow<Int> = _userId.asStateFlow()

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _accessToken = MutableStateFlow("")
    val accessToken: StateFlow<String> = _accessToken.asStateFlow()

    private val _refreshToken = MutableStateFlow("")
    val refreshToken: StateFlow<String> = _refreshToken.asStateFlow()

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

    private val _isOnboardingShown = MutableStateFlow(false)
    val isOnboardingShown: StateFlow<Boolean> = _isOnboardingShown.asStateFlow()


    init {
        viewModelScope.launch {

            launch {
                authRepository.readUserIdFromDataStore.collect { userIdValue ->
                    _userId.value = userIdValue
                }
            }

            launch {
                authRepository.readUsernameFromDataStore.collect { usernameValue ->
                    _username.value = usernameValue
                }
            }

            launch {
                authRepository.readEmailFromDataStore.collect { emailValue ->
                    _email.value = emailValue
                }
            }

            launch {
                authRepository.readPasswordFromDataStore.collect { passwordValue ->
                    _password.value = passwordValue
                }
            }

            launch {
                authRepository.readRefreshTokenFromDataStore.collect { refreshTokenValue ->
                    _refreshToken.value = refreshTokenValue
                }
            }

            launch {
                authRepository.readAccessTokenFromDataStore.collect { accessTokenValue ->
                    _accessToken.value = accessTokenValue
                }
            }

            launch {
                authRepository.readIsUserLoggedInFromDataStore.collect { isUserLoggedInValue ->
                    _isUserLoggedIn.value = isUserLoggedInValue
                }
            }

            launch {
                authRepository.readIsOnboardingShownFromDataStore.collect { isOnboardingShownValue ->
                    _isOnboardingShown.value = isOnboardingShownValue
                }
            }

        }
    }

    fun saveUserIdToDataStore(userId: Int) = viewModelScope.launch(Dispatchers.IO) {
        authRepository.saveUserIdToDataStore(userId)
    }

    fun saveUsernameToDataStore(username: String) = viewModelScope.launch(Dispatchers.IO) {
        authRepository.saveUsernameToDataStore(username)
    }

    fun saveEmailToDataStore(email: String) = viewModelScope.launch(Dispatchers.IO) {
        authRepository.saveEmailToDataStore(email)
    }

    fun savePasswordToDataStore(password: String) = viewModelScope.launch(Dispatchers.IO) {
        authRepository.savePasswordToDataStore(password)
    }

    fun saveRefreshTokenToDataStore(refreshToken: String) = viewModelScope.launch(Dispatchers.IO) {
        authRepository.saveRefreshTokenToDataStore(refreshToken)
    }

    fun saveAccessTokenToDataStore(accessToken: String) = viewModelScope.launch(Dispatchers.IO) {
        authRepository.saveAccessTokenToDataStore(accessToken)
    }

    fun saveIsOnboardingShownToDataStore(isOnboardingShown: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.saveIsOnboardingShowToDataStore(isOnboardingShown)
        }

    fun saveIsUserLoggedInToDataStore(isUserLoggedIn: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.saveIsUserLoggedIn(isUserLoggedIn)
        }

    fun loginUser(email: String, password: String) {
        _loginUiState.value = LoginUiState.Loading

        viewModelScope.launch {
            try {
                val result = authRepository.loginUser(email, password)
                result.onSuccess { loginResponse ->
                    saveUserIdToDataStore(loginResponse.id)
                    saveUsernameToDataStore(loginResponse.username)
                    saveEmailToDataStore(email)
                    savePasswordToDataStore(password)
                    saveIsOnboardingShownToDataStore(true)
                    saveIsUserLoggedInToDataStore(true)
                    saveRefreshTokenToDataStore(loginResponse.refresh)
                    saveAccessTokenToDataStore(loginResponse.access)
                    _loginUiState.value = LoginUiState.Success(loginResponse)
                }.onFailure { throwable ->
                    _loginUiState.value = LoginUiState.Error(throwable.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _loginUiState.value = LoginUiState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun registerUser(email: String, username: String, password: String) {
        _registerUiState.value = RegisterUiState.Loading

        viewModelScope.launch {
            try {
                val result = authRepository.registerUser(email, username, password)
                result.onSuccess { registerResponse ->
                    _registerUiState.value = RegisterUiState.Success(registerResponse)
                }.onFailure { throwable ->
                    _registerUiState.value =
                        RegisterUiState.Error(throwable.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _registerUiState.value = RegisterUiState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun loginSupabaseUser(email: String, password: String) {
        viewModelScope.launch {
            _loginSupabaseUiState.value = LoginSupabaseUiState.Loading
            try {
                val response = SupabaseClient.client.gotrue.loginWith(Email) {
                    this.email = email
                    this.password = password
                }

                // Get the session details
                val session = SupabaseClient.client.gotrue.currentSessionOrNull()

                if (session != null) {
                    // Store the session
                    saveAccessTokenToDataStore(session.accessToken)
                    saveRefreshTokenToDataStore(session.refreshToken)
                    saveEmailToDataStore(email)
                    savePasswordToDataStore(password)
                    saveIsUserLoggedInToDataStore(true)

                    _loginSupabaseUiState.value = LoginSupabaseUiState.Success(
                        LoginResponseSupabase(
                            accessToken = session.accessToken,
                            refreshToken = session.refreshToken
                        )
                    )
                } else {
                    _loginSupabaseUiState.value =
                        LoginSupabaseUiState.Error("Failed to get session")
                }
            } catch (e: Exception) {
                _loginSupabaseUiState.value =
                    LoginSupabaseUiState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun signUpSupabaseUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = SupabaseClient.client.gotrue.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }
                // Handle successful signup
            } catch (e: Exception) {
                // Handle signup error
            }
        }
    }

    fun logoutSupabaseUser() {
        viewModelScope.launch {
            try {
                SupabaseClient.client.gotrue.logout()
                // Clear local storage
                saveUsernameToDataStore("")
                saveEmailToDataStore("")
                savePasswordToDataStore("")
                saveAccessTokenToDataStore("")
                saveRefreshTokenToDataStore("")
                saveIsUserLoggedInToDataStore(false)
            } catch (e: Exception) {
                // Handle logout error
            }
        }
    }

}

sealed class LoginUiState {
    object Loading : LoginUiState()
    data class Success(val user: LoginResponse) : LoginUiState()
    data class Error(val exception: String) : LoginUiState()
    object Idle : LoginUiState() // Added to represent the initial state
}

sealed class LoginSupabaseUiState {
    object Loading : LoginSupabaseUiState()
    data class Success(val user: LoginResponseSupabase) : LoginSupabaseUiState()
    data class Error(val exception: String) : LoginSupabaseUiState()
    object Idle : LoginSupabaseUiState() // Added to represent the initial state
}

sealed class RegisterUiState {
    object Loading : RegisterUiState()
    data class Success(val user: RegisterResponse) : RegisterUiState()
    data class Error(val exception: String) : RegisterUiState()
    object Idle : RegisterUiState() // Added to represent the initial state
}