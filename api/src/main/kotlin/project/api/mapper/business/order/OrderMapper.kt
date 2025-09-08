package project.api.mapper.business.order

import project.api.dto.request.business.OrderDtoRequest
import project.api.dto.response.business.OrderDtoResponse
import project.api.entity.Order
import project.api.entity.User

interface OrderMapper {
    fun toOrder(request: OrderDtoRequest, user: User): Order
    fun toDto(order: Order): OrderDtoResponse
}