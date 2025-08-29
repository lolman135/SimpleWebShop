package project.api.mapper.business.order

import project.api.dto.business.OrderDto
import project.api.entity.Order
import project.api.entity.User

interface OrderMapper {
    fun toOrder(orderDto: OrderDto, user: User): Order
}