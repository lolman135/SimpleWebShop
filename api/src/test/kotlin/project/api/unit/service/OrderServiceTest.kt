package project.api.unit.service

import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import project.api.dto.OrderDto
import project.api.entity.Order
import project.api.entity.User
import project.api.mapper.order.OrderMapper
import project.api.repository.order.OrderRepository
import project.api.service.business.order.OrderServiceImpl
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class OrderServiceTest {

    @Mock
    private lateinit var orderRepository: OrderRepository

    @Mock
    private lateinit var orderMapper: OrderMapper

    @InjectMocks
    private lateinit var orderService: OrderServiceImpl

    private lateinit var orderId: UUID
    private lateinit var orderDto: OrderDto
    private lateinit var user: User
    private lateinit var order: Order

}