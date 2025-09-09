package project.api.unit.mappers

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import project.api.dto.request.business.ItemDto
import project.api.dto.request.business.OrderDtoRequest
import project.api.entity.Category
import project.api.entity.Order
import project.api.entity.Product
import project.api.entity.User
import project.api.mapper.business.order.OrderMapperImpl
import project.api.repository.product.ProductRepository
import java.time.LocalDateTime
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExtendWith(MockitoExtension::class)
class OrderMapperTest {

    @Mock
    lateinit var productRepository: ProductRepository

    @InjectMocks
    lateinit var orderMapper: OrderMapperImpl

    private lateinit var testUser: User
    private lateinit var testProduct: Product
    private lateinit var orderDto: OrderDtoRequest
    private lateinit var productId: UUID
    private lateinit var category: Category

    @BeforeEach
    fun setUp() {
        productId = UUID.randomUUID()
        category = Category(
            id = UUID.randomUUID(),
            name = "Test category"
        )

        testUser = User(
            id = UUID.randomUUID(),
            username = "testUName",
            email = "test@mail.com",
            password = "testPassword"
        )

        testProduct = Product(
            id = productId,
            name = "testName",
            description = "TestDesc",
            imageUrl = "https://imgBase:/testImg.com",
            price = 10,
            category = category
        )

        orderDto = OrderDtoRequest(
            mutableListOf(ItemDto(productId, 3))
        )
    }

    @Test
    fun toOrderShouldContainsCorrectData() {
        `when`(productRepository.findById(productId)).thenReturn(Optional.of(testProduct))

        val order = orderMapper.toOrder(orderDto, testUser)

        assertEquals(testUser, order.user)
        assertEquals(1, order.products.size)
        assertEquals(30, order.totalCost)
        assertEquals(null, order.id)
        verify(productRepository).findById(productId)
    }

    @Test
    fun toOrderThrowsExceptionForInvalidProductId() {
        `when`(productRepository.findById(productId)).thenReturn(Optional.empty())

        assertFailsWith<IllegalArgumentException> {
            orderMapper.toOrder(orderDto, testUser)
        }
        verify(productRepository).findById(productId)
    }

    @Test
    fun toOrderShouldFailsByProvidingEmptyList(){
        val invalidOrderDto = OrderDtoRequest()

        assertFailsWith<IllegalArgumentException> {
            orderMapper.toOrder(invalidOrderDto, testUser)
        }
    }

    @Test
    fun toDtoShouldMapCorrectly() {
        val orderId = UUID.randomUUID()
        val order = Order(
            id = orderId,
            createdAt = java.time.LocalDateTime.now(),
            totalCost = 30,
            user = testUser,
            products = mutableSetOf(testProduct)
        )

        val dto = orderMapper.toDto(order)

        assertEquals(orderId, dto.id)
        assertEquals(30, dto.totalCost)
        assertEquals(testUser.id, dto.user.id)
        assertEquals(testUser.username, dto.user.username)
        assertEquals(testUser.email, dto.user.email)

        assertEquals(1, dto.products.size)
        val productDto = dto.products.first()
        assertEquals(testProduct.id, productDto.id)
        assertEquals(testProduct.name, productDto.name)
        assertEquals(testProduct.price, productDto.price)
    }

    @Test
    fun toDtoShouldThrowExceptionWhenIdIsNull() {
        val order = Order(
            id = null,
            createdAt = LocalDateTime.now(),
            totalCost = 10,
            user = testUser,
            products = mutableSetOf(testProduct)
        )

        val exception = assertFailsWith<IllegalArgumentException> {
            orderMapper.toDto(order)
        }
        assertEquals("Id is not provided", exception.message)
    }
}
