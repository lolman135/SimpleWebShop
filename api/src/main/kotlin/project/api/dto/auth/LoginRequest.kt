package project.api.dto.auth

import jakarta.validation.constraints.Pattern

data class LoginRequest(
    @field:Pattern(regexp = "^[a-zA-Z-_\\d\\s]{3,40}\$", message = "Invalid input")
    val username: String,
    @field:Pattern(regexp = "^[a-zA-Z-_:#\\d%+]{7,30}", message = "Invalid input")
    val password: String
)
