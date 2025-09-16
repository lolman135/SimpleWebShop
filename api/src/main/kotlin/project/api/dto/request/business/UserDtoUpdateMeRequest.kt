package project.api.dto.request.business

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern

data class UserDtoUpdateMeRequest(
    @field:Pattern(regexp = "^[a-zA-Zа-яА-ЯіїІЇ_\\d\\s-]{3,40}\$", message = "Invalid username")
    val username: String? = null,

    @field:Email(message = "Invalid Email")
    val email: String? = null,

    @field:Pattern(regexp = "^[a-zA-Z-_:#\\d%+]{7,30}", message = "Invalid input")
    val password: String? = null
)
