package project.api.dto.business

import jakarta.validation.constraints.Pattern

data class RoleDto(

    @Pattern(
        regexp = "^[A-Z_]{2,30}$",
        message = "Invalid name"
    )
    val name: String
)