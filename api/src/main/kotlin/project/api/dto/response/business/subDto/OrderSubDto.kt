package project.api.dto.response.business.subDto

import java.util.UUID

data class OrderSubDto (
    val id: UUID,
    val totalCost: Int,
    val productIds: List<UUID>
)