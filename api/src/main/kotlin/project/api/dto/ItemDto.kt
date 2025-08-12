package project.api.dto

import jakarta.validation.constraints.Min
import java.util.*

data class ItemDto(
    val productId: UUID,

    @Min(0)
    val productsAmount: Int
)
