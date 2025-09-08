package project.api.service.business.order

import project.api.dto.request.business.OrderDtoRequest
import project.api.entity.Order
import project.api.entity.User
import java.util.UUID

interface OrderService {
    fun save(dto: OrderDtoRequest, user: User): Order
    fun findAll(): List<Order>
    fun findAllForUser(user: User): List<Order>
    fun findById(id: UUID): Order
    fun updateById(id: UUID, dto: OrderDtoRequest, user: User): Order
    fun deleteById(id: UUID): Boolean
}