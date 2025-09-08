package project.api.dto.request.business

import jakarta.validation.constraints.Pattern

data class CategoryDtoRequest(
    @field:Pattern(regexp = "^[a-zA-Z\\s-_:]{2,40}\$", message = "Invalid name")
    val name: String
)
