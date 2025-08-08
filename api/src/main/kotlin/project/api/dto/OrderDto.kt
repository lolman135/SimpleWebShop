package project.api.dto

data class OrderDto(
    val items: MutableList<ItemDto> = mutableListOf()
)
