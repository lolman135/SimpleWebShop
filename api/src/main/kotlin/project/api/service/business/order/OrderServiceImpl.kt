package project.api.service.business.order

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import project.api.dto.request.business.OrderDtoRequest
import project.api.dto.response.business.OrderDtoResponse
import project.api.entity.User
import project.api.exception.EntityNotFoundException
import project.api.mapper.business.order.OrderMapper
import project.api.repository.order.OrderRepository
import java.util.*

@Service
class OrderServiceImpl (
    private val orderMapper: OrderMapper,
    private val orderRepository: OrderRepository
): OrderService {

    override fun deleteById(id: UUID): Boolean {
        if (!orderRepository.existsById(id))
            throw EntityNotFoundException("Order with id=$id not found")
        orderRepository.deleteById(id)
        return true
    }

    @Transactional
    override fun save(dto: OrderDtoRequest, user: User): OrderDtoResponse {
        val order = orderMapper.toOrder(dto, user)
        val savedOrder = orderRepository.save(order)
        return orderMapper.toDto(savedOrder)
    }

    @Transactional
    override fun findAll() = orderRepository.findAll().map { orderMapper.toDto(it) }

    @Transactional
    override fun findAllForUser(user: User) = user.orders.map{ orderMapper.toDto(it) }.toList()

    @Transactional
    override fun findById(id: UUID): OrderDtoResponse {
        val order = orderRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Order with id=$id not found") }
        return orderMapper.toDto(order)
    }

    @Transactional
    override fun updateById(id: UUID, dto: OrderDtoRequest, user: User): OrderDtoResponse {
        if (!orderRepository.existsById(id))
            throw EntityNotFoundException("Order with id=$id not found")
        val order = orderMapper.toOrder(dto, user)
        order.id = id
        val updatedOrder = orderRepository.save(order)
        return orderMapper.toDto(updatedOrder)
    }
}