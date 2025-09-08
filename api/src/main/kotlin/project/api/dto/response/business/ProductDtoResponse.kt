package project.api.dto.response.business

import project.api.dto.response.business.subDto.FeedbackSubDto
import java.util.UUID

data class ProductDtoResponse(
    val id: UUID,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val description: String,
    val feedbacks: List<FeedbackSubDto>,
    val category: String
)
