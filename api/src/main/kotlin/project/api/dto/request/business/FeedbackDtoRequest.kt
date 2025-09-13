package project.api.dto.request.business

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import java.util.UUID

data class FeedbackDtoRequest(
    @field:Pattern(regexp = "^[a-zA-Zа-яА-Я\\d\\s.?:;,№#!\"]+$", message = "Invalid feedback")
    val review: String? = null,
    @field:Min(0) @field:Max(5)
    val rate: Int,
    val productId: UUID,
)