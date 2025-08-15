package project.api.service.business.order

import project.api.dto.OrderDto
import project.api.entity.Order
import project.api.entity.User
import java.util.UUID

interface OrderService {
    fun save(dto: OrderDto, user: User)
    fun findAll(): List<Order>
    fun findAllForUser(user: User): List<Order>
    fun findById(id: UUID): Order
    fun updateById(id: UUID, orderDto: OrderDto): Order
    fun deleteById(id: UUID): Boolean
}