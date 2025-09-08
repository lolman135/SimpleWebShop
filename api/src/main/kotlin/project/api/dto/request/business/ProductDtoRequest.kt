package project.api.dto.request.business

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import java.util.*

data class ProductDtoRequest(
    @field:Pattern(regexp = "^[a-zA-Z\\d\\s-_:]{2,50}$", message = "Invalid name!")
    val name: String,

    @field:NotBlank(message = "Description can't be empty!")
    val description: String,

    @field:Min(0)
    val price: Int,

    val imageUrl: String,
    val feedbackIds: List<UUID> = listOf(),
    val categoryId: UUID
)
