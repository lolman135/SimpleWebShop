package project.api.mapper.order

import project.api.dto.OrderDto
import project.api.entity.Order
import project.api.entity.User

interface OrderMapper {
    fun mapToOrder(orderDto: OrderDto, user: User): Order
}