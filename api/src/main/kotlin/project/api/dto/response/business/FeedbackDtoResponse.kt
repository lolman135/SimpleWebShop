package project.api.dto.response.business

import project.api.dto.response.business.subDto.ProductSubDto
import project.api.dto.response.business.subDto.UserSubDto
import java.util.UUID

data class FeedbackDtoResponse(
    val id: UUID,
    val review: String,
    val rate: Int,
    val user: UserSubDto,
    val product: ProductSubDto
)
