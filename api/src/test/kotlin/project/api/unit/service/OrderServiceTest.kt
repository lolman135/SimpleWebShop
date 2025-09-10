package project.api.unit.service

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import project.api.dto.request.business.ItemDto
import project.api.dto.request.business.OrderDtoRequest
import project.api.dto.response.business.OrderDtoResponse
import project.api.dto.response.business.subDto.UserSubDto
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

    @Mock private lateinit var orderRepository: OrderRepository
    @Mock private lateinit var orderMapper: OrderMapper

    @InjectMocks private lateinit var orderService: OrderServiceImpl

    private lateinit var orderId: UUID
    private lateinit var productId: UUID
    private lateinit var userId: UUID
    private lateinit var orderDto: OrderDtoRequest
    private lateinit var user: User
    private lateinit var order: Order
    private lateinit var orderDtoResponse: OrderDtoResponse

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
        orderDto = OrderDtoRequest(
            mutableListOf(ItemDto(productId, 3))
        )
        order = Order(
            id = orderId,
            createdAt = LocalDateTime.now(),
            totalCost = 3000,
            user = user
        )

        orderDtoResponse = OrderDtoResponse(
            id = orderId,
            createdAt = order.createdAt,
            totalCost = order.totalCost,
            products = emptyList(),
            user = UserSubDto(user.id!!, user.username, user.email)
        )
    }

    @Test
    fun saveShouldMapDtoToOrderAndSaveEntity(){
        `when`(orderMapper.toOrder(orderDto, user)).thenReturn(order)
        `when`(orderRepository.save(order)).thenReturn(order)
        `when`(orderMapper.toDto(order)).thenReturn(orderDtoResponse)

        val result = orderService.save(orderDto, user)

        assertEquals(orderDtoResponse, result)
        verify(orderMapper).toOrder(orderDto, user)
        verify(orderRepository).save(order)
        verify(orderMapper).toDto(order)
    }

    @Test
    fun findAllShouldReturnListOfOrderDtoResponses(){
        val orders = listOf(order)
        `when`(orderRepository.findAll()).thenReturn(orders)
        `when`(orderMapper.toDto(order)).thenReturn(orderDtoResponse)

        val result = orderService.findAll()

        assertEquals(listOf(orderDtoResponse), result)
        verify(orderRepository).findAll()
        verify(orderMapper).toDto(order)
    }

    @Test
    fun findByIdShouldReturnOrderDtoResponseIfExists() {
        `when`(orderRepository.findById(orderId)).thenReturn(Optional.of(order))
        `when`(orderMapper.toDto(order)).thenReturn(orderDtoResponse)

        val result = orderService.findById(orderId)

        assertEquals(orderDtoResponse, result)
        verify(orderRepository).findById(orderId)
        verify(orderMapper).toDto(order)
    }

    @Test
    fun findByIdShouldThrowExceptionWhenOrderDoesNotExist() {
        `when`(orderRepository.findById(orderId)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            orderService.findById(orderId)
        }

        verify(orderRepository).findById(orderId)
        verify(orderMapper, never()).toDto(order)
    }

    @Test
    fun updateByIdShouldUpdateOrderWhenExists() {
        `when`(orderRepository.existsById(orderId)).thenReturn(true)
        `when`(orderMapper.toOrder(orderDto, user)).thenReturn(order)
        `when`(orderRepository.save(order)).thenReturn(order)
        `when`(orderMapper.toDto(order)).thenReturn(orderDtoResponse)

        val result = orderService.updateById(orderId, orderDto, user)

        assertEquals(orderDtoResponse, result)
        verify(orderRepository).existsById(orderId)
        verify(orderMapper).toOrder(orderDto, user)
        verify(orderRepository).save(order)
        verify(orderMapper).toDto(order)
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
        verify(orderMapper, never()).toDto(order)
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
