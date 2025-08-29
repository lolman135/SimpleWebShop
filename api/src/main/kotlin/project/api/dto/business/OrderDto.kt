package project.api.dto.business

data class OrderDto(
    val items: List<ItemDto> = listOf()
)
