package project.api.dto.request.business

data class OrderDtoRequest(
    val items: List<ItemDto> = listOf()
)
