package project.api.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import java.util.*

data class ProductDto(

    @Pattern(regexp = "^[a-zA-Z\\d\\s-_:]{2,50}$", message = "Invalid name!")
    val name: String,

    @NotBlank(message = "Description can't be empty!")
    val description: String,

    @Min(0)
    val price: Int,

    val imageUrl: String,
    val feedbackIds: MutableList<UUID> = mutableListOf()
)
