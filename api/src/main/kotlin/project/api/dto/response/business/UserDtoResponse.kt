package project.api.dto.response.business

import project.api.dto.response.business.subDto.FeedbackSubDto
import project.api.dto.response.business.subDto.OrderSubDto
import java.util.UUID

data class UserDtoResponse(
    val id: UUID,
    val username: String,
    val email: String,
    val orders: List<OrderSubDto>,
    val feedbacks: List<FeedbackSubDto>,
    val roles: List<String>
)