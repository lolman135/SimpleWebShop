package project.api.dto.response.business

import java.time.LocalDateTime
import java.util.UUID
import project.api.dto.response.business.subDto.ProductSubDto
import project.api.dto.response.business.subDto.UserSubDto

data class OrderDtoResponse(
    val id: UUID,
    val createdAt: LocalDateTime,
    val totalCost: Int,
    val products: List<ProductSubDto>,
    val user: UserSubDto
)
