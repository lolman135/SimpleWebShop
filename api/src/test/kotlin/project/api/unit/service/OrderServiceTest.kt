package project.api.unit.service

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import project.api.dto.business.ItemDto
import project.api.dto.business.OrderDto
import project.api.entity.Order
import project.api.entity.User
import project.api.exception.EntityNotFoundException
import project.api.mapper.business.order.OrderMapper
import project.api.repository.order.OrderRepository
import project.api.service.business.order.OrderServiceImpl
import java.time.LocalDateTime
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class OrderServiceTest {

    @Mock
    private lateinit var orderRepository: OrderRepository
    @Mock
    private lateinit var orderMapper: OrderMapper

    @InjectMocks
    private lateinit var orderService: OrderServiceImpl

    private lateinit var orderId: UUID
    private lateinit var productId: UUID
    private lateinit var userId: UUID
    private lateinit var orderDto: OrderDto
    private lateinit var user: User
    private lateinit var order: Order


    @BeforeEach
    fun setUp(){
        orderId = UUID.randomUUID()
        productId = UUID.randomUUID()
        userId = UUID.randomUUID()

        user = User(
            id = userId,
            username = "JohnDoe",
            email = "john@example.com",
            password = "securePass123",
        )
        orderDto = OrderDto(
            mutableListOf(ItemDto(productId, 3))
        )
        order = Order(
            id = orderId,
            createdAt = LocalDateTime.now(),
            totalCost = 3000,
            user = user
        )
    }

    @Test
    fun saveShouldMapDtoToOrderAndSaveEntity(){
        `when`(orderMapper.toOrder(orderDto, user)).thenReturn(order)
        `when`(orderRepository.save(order)).thenReturn(order)

        val result = orderService.save(orderDto, user)
        assertEquals(order, result)
        verify(orderMapper).toOrder(orderDto, user)
        verify(orderRepository).save(order)
    }

    @Test
    fun findAllShouldReturnListOfOrders(){
        val orders = listOf(order)
        `when`(orderRepository.findAll()).thenReturn(orders)

        val result = orderService.findAll()
        assertEquals(orders, result)
        verify(orderRepository).findAll()
    }

    @Test
    fun findByIdShouldReturnOrderIfExists() {
        `when`(orderRepository.findById(orderId)).thenReturn(Optional.of(order))

        val result = orderService.findById(orderId)

        assertEquals(order, result)
        verify(orderRepository).findById(orderId)
    }

    @Test
    fun findByIdShouldThrowExceptionWhenOrderDoesNotExist() {
        `when`(orderRepository.findById(orderId)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            orderService.findById(orderId)
        }
        verify(orderRepository).findById(orderId)
    }

    @Test
    fun updateByIdShouldUpdateOrderWhenExists() {
        `when`(orderRepository.existsById(orderId)).thenReturn(true)
        `when`(orderMapper.toOrder(orderDto, user)).thenReturn(order)
        `when`(orderRepository.save(order)).thenReturn(order)

        val result = orderService.updateById(orderId, orderDto, user)

        assertEquals(order, result)
        verify(orderRepository).existsById(orderId)
        verify(orderMapper).toOrder(orderDto, user)
        verify(orderRepository).save(order)
    }

    @Test
    fun updateByIdShouldThrowExceptionWhenOrderDoesNotExist() {
        `when`(orderRepository.existsById(orderId)).thenReturn(false)

        assertThrows<EntityNotFoundException> {
            orderService.updateById(orderId, orderDto, user)
        }

        verify(orderRepository).existsById(orderId)
        verify(orderMapper, never()).toOrder(orderDto, user)
        verify(orderRepository, never()).save(order)
    }

    @Test
    fun deleteByIdShouldDeleteOrderWhenExists() {
        `when`(orderRepository.existsById(orderId)).thenReturn(true)

        val isDeleted = orderService.deleteById(orderId)

        assertTrue(isDeleted)
        verify(orderRepository).existsById(orderId)
        verify(orderRepository).deleteById(orderId)
    }

    @Test
    fun deleteByIdShouldThrowExceptionWhenOrderDoesNotExist() {
        `when`(orderRepository.existsById(orderId)).thenReturn(false)

        assertThrows<EntityNotFoundException> {
            orderService.deleteById(orderId)
        }

        verify(orderRepository).existsById(orderId)
        verify(orderRepository, never()).deleteById(orderId)
    }
}