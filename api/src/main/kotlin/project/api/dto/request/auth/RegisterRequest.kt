package project.api.dto.request.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern

data class RegisterRequest(
    @field:Pattern(regexp = "^[a-zA-Z-_\\d\\s]{3,40}\$", message = "Invalid input")
    val username: String,

    @field:Email(message = "Invalid Email")
    val email: String,

    @field:Pattern(regexp = "^[a-zA-Z-_:#\\d%+]{7,30}", message = "Invalid input")
    val password: String
)
