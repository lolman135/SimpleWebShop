package project.api.dto

import jakarta.validation.constraints.Pattern

data class UserDto(

    @Pattern(regexp = "^[a-zA-Z-_\\d]{3,40}\$", message = "Invalid input")
    val username: String,

    @Pattern(
        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$",
        message = "Invalid input"
    )
    val email: String,

    @Pattern(regexp = "^[a-zA-Z-_:#\\d%+]{7,30}", message = "Invalid input")
    val password: String
)
