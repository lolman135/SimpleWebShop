package project.api.dto.response.business.subDto

import java.util.UUID

data class FeedbackSubDto(
    val id: UUID,
    val review: String = "",
    val rate: Int
)
