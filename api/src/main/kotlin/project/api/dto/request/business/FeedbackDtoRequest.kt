package project.api.dto.request.business

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.util.UUID

data class FeedbackDtoRequest(
    @field:NotBlank(message = "Shouldn't be empty") @field:Min(3)
    val review: String? = null,
    @field:Min(0) @field:Max(5)
    val rate: Int,
    val productId: UUID,
)