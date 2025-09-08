package project.api.dto.request.business

import jakarta.validation.constraints.Min
import java.util.*

data class ItemDto(
    val productId: UUID,
    @field:Min(0)
    val productsAmount: Int
)
