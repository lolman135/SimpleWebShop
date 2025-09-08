package project.api.service.business.order

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import project.api.dto.request.business.OrderDtoRequest
import project.api.entity.Order
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
    override fun save(dto: OrderDtoRequest, user: User): Order {
        val order = orderMapper.toOrder(dto, user)
        return orderRepository.save(order)
    }

    @Transactional
    override fun findAll() = orderRepository.findAll()

    @Transactional
    override fun findAllForUser(user: User) = user.orders.toList()

    @Transactional
    override fun findById(id: UUID) = orderRepository.findById(id).orElseThrow {
        EntityNotFoundException("Order with id=$id not found")
    }

    @Transactional
    override fun updateById(id: UUID, dto: OrderDtoRequest, user: User): Order {
        if (!orderRepository.existsById(id))
            throw EntityNotFoundException("Order with id=$id not found")
        val updatedOrder = orderMapper.toOrder(dto, user)
        updatedOrder.id = id
        return orderRepository.save(updatedOrder)
    }
}