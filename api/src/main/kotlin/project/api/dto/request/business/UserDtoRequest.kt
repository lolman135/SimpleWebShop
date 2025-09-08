package project.api.dto.request.business

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import java.util.*

data class UserDtoRequest(

    @field:Pattern(regexp = "^[a-zA-Z-_\\d]{3,40}\$", message = "Invalid username")
    val username: String,

    @field:Email(message = "Invalid Email")
    val email: String,

    @field:Pattern(regexp = "^[a-zA-Z-_:#\\d%+]{7,30}", message = "Invalid input")
    val password: String,

    val orderIds: List<UUID> = listOf(),
    val roleIds: List<UUID> = listOf(),
    val feedbackIds: List<UUID> = listOf(),
)
