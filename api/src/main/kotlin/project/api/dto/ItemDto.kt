package project.api.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.util.*

data class ItemDto(
    @NotNull
    val productId: UUID,

    @Min(0)
    val productsAmount: Int
)
