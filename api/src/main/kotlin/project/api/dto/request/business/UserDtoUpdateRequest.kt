package project.api.dto.request.business

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import java.util.*

data class UserDtoUpdateRequest(

    @field:Pattern(regexp = "^[a-zA-Z-_\\d]{3,40}\$", message = "Invalid username")
    val username: String? = null,

    @field:Email(message = "Invalid Email")
    val email: String? = null,

    @field:Pattern(regexp = "^[a-zA-Z-_:#\\d%+]{7,30}", message = "Invalid input")
    val password: String? = null,

    val orderIds: List<UUID>? = null,
    val roleIds: List<UUID>? = null,
    val feedbackIds: List<UUID>? = null,
)
