package project.api.dto.request.business

import jakarta.validation.constraints.Pattern

data class RoleDtoRequest(

    @field:Pattern(regexp = "^[A-Z_]{2,30}$", message = "Invalid name")
    val name: String
)