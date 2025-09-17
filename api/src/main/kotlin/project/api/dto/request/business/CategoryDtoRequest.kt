package project.api.dto.request.business

import com.fasterxml.jackson.annotation.JsonTypeInfo
import jakarta.validation.constraints.Pattern

data class CategoryDtoRequest(
    @field:Pattern(regexp = "^[a-zA-Z\\s-_:]{2,40}\$", message = "Invalid name")
    val name: String
)
