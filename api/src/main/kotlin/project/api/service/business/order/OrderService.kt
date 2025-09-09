package project.api.service.business.order

import project.api.dto.request.business.OrderDtoRequest
import project.api.dto.response.business.OrderDtoResponse
import project.api.entity.User
import java.util.UUID

interface OrderService {
    fun save(dto: OrderDtoRequest, user: User): OrderDtoResponse
    fun findAll(): List<OrderDtoResponse>
    fun findAllForUser(user: User): List<OrderDtoResponse>
    fun findById(id: UUID): OrderDtoResponse
    fun updateById(id: UUID, dto: OrderDtoRequest, user: User): OrderDtoResponse
    fun deleteById(id: UUID): Boolean
}