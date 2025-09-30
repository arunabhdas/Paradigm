package app.paradigmatic.paradigmaticapp.presentation.data.model



data class LoginResponse(
    val access: String,
    val email: String,
    val firstName: String,
    val id: Int,
    val isActive: Boolean,
    val isDeactivated: Boolean,
    val lastName: String,
    val refresh: String,
    val username: String
)