package project.api.dto

import jakarta.validation.constraints.Pattern
import java.util.*


data class RoleDto(

    @Pattern(
        regexp = "^[A-Z_]{2,30}$",
        message = "Invalid name"
    )
    val name: String
)