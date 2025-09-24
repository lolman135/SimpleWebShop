package project.api.service.business.order

import jakarta.transaction.Transactional
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
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

    @Caching(
        evict = [
            CacheEvict(value = ["orderList"], allEntries = true),
            CacheEvict(value = ["orderForUser"], allEntries = true),
            CacheEvict(value = ["orders"], key = "#id"),
            CacheEvict(value = ["userList"], allEntries = true),
            CacheEvict(value = ["users"], allEntries = true)
        ]
    )
    override fun deleteById(id: UUID): Boolean {
        orderRepository.deleteById(id)
        return true
    }

    @Transactional
    @Caching(
        evict = [
            CacheEvict(value = ["orderList"], allEntries = true),
            CacheEvict(value = ["orderForUser"], key = "#user.username"),
            CacheEvict(value = ["userList"], allEntries = true),
            CacheEvict(value = ["users"], key = "#user.id")
        ]
    )
    override fun save(dto: OrderDtoRequest, user: User): OrderDtoResponse {
        val order = orderMapper.toOrder(dto, user)
        val savedOrder = orderRepository.save(order)
        return orderMapper.toDto(savedOrder)
    }

    @Transactional
    @Cacheable(value = ["orderList"])
    override fun findAll() = orderRepository.findAll().map { orderMapper.toDto(it) }

    @Transactional
    @Cacheable(value = ["ordersForUser"], key = "#user.username")
    override fun findAllForUser(user: User) = user.orders.map{ orderMapper.toDto(it) }.toList()

    @Transactional
    @Cacheable(value = ["orders"], key = "#id")
    override fun findById(id: UUID): OrderDtoResponse {
        val order = orderRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Order with id=$id not found") }
        return orderMapper.toDto(order)
    }

    @Transactional
    @Caching(
        put = [CachePut(value = ["orders"], key = "#id")],
        evict = [
            CacheEvict(value = ["orderList"], allEntries = true),
            CacheEvict(value = ["ordersForUser"], key = "#user.username"),
            CacheEvict(value = ["userList"], allEntries = true),
            CacheEvict(value = ["users"], key = "#user.id")
        ]
    )
    override fun updateById(id: UUID, dto: OrderDtoRequest, user: User): OrderDtoResponse {
        if (!orderRepository.existsById(id))
            throw EntityNotFoundException("Order with id=$id not found")
        val order = orderMapper.toOrder(dto, user)
        order.id = id
        val updatedOrder = orderRepository.save(order)
        return orderMapper.toDto(updatedOrder)
    }
}