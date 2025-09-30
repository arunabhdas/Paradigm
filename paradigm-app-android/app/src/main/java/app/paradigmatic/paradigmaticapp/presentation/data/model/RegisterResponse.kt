package app.paradigmatic.paradigmaticapp.presentation.data.model



data class RegisterResponse(
    val email: String,
    val firstName: String,
    val id: Int,
    val lastName: String,
    val username: String
)