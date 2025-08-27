package project.api.service.business.order

import org.springframework.stereotype.Service
import project.api.dto.OrderDto
import project.api.entity.Order
import project.api.entity.User
import project.api.exception.EntityNotFoundException
import project.api.mapper.order.OrderMapper
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

    override fun save(dto: OrderDto, user: User): Order {
        val order = orderMapper.toOrder(dto, user)
        return orderRepository.save(order)
    }

    override fun findAll() = orderRepository.findAll()

    override fun findAllForUser(user: User) = user.orders.toList()


    override fun findById(id: UUID) = orderRepository.findById(id).orElseThrow {
        EntityNotFoundException("Order with id=$id not found")
    }

    override fun updateById(id: UUID, dto: OrderDto, user: User): Order {
        if (!orderRepository.existsById(id))
            throw EntityNotFoundException("Order with id=$id not found")
        val updatedOrder = orderMapper.toOrder(dto, user)
        updatedOrder.id = id
        return orderRepository.save(updatedOrder)
    }
}