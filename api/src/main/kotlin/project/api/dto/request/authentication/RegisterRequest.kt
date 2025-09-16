package project.api.dto.request.authentication

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern

data class RegisterRequest(
    @field:Pattern(regexp = "^[a-zA-Zа-яА-ЯіїІЇ_\\d\\s-]{3,40}\$", message = "Invalid username")
    val username: String,
    @field:Email(message = "Invalid Email")
    val email: String,
    @field:Pattern(regexp = "^[a-zA-Z-_:#\\d%+]{7,30}", message = "Invalid input")
    val password: String
)
