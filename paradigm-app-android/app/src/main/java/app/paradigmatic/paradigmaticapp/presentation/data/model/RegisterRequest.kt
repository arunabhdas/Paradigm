package app.paradigmatic.paradigmaticapp.presentation.data.model


data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String
)