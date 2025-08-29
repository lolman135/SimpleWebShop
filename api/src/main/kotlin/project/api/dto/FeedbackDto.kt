package project.api.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.util.UUID

data class FeedbackDto(
    @NotBlank(message = "Shouldn't be empty")
    val review: String? = null,
    @Min(0) @Max(5)
    val rate: Int,
    val userId: UUID,
    val productId: UUID,
)