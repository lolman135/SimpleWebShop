package project.api.dto

import jakarta.validation.constraints.Pattern
import java.util.*


data class RoleDto(
    val id: UUID? = null,

    @Pattern(
        regexp = "^[A-Z]{2,30}$",
        message = "Name of role can use only capital latin letters"
    )
    val name: String
)