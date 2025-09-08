package project.api.dto.response.business.subDto

import java.util.UUID

data class ProductSubDto(
    val id: UUID,
    val name: String,
    val price: Int
)
