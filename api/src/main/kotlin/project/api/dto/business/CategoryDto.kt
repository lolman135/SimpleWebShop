package project.api.dto.business

import jakarta.validation.constraints.Pattern

data class CategoryDto(
    @field:Pattern(regexp = "^[a-zA-Z\\s-_:]{2,40}\$", message = "Invalid name")
    val name: String
)
