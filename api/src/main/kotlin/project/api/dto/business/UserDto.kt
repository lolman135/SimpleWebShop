package project.api.dto.business

import jakarta.validation.constraints.Pattern
import java.util.*

data class UserDto(

    @Pattern(regexp = "^[a-zA-Z-_\\d]{3,40}\$", message = "Invalid input")
    val username: String,

    @Pattern(
        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$",
        message = "Invalid input"
    )
    val email: String,

    @Pattern(regexp = "^[a-zA-Z-_:#\\d%+]{7,30}", message = "Invalid input")
    val password: String,

    val orderIds: List<UUID> = listOf(),
    val roleIds: List<UUID> = listOf(),
    val feedbackIds: List<UUID> = listOf(),
)
