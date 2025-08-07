package project.api.mappers

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import project.api.dto.ItemDto
import project.api.dto.OrderDto
import project.api.entity.Product
import project.api.entity.User
import project.api.mapper.order.OrderMapperImpl
import project.api.repository.product.ProductRepository
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
    private lateinit var orderDto: OrderDto

    @BeforeEach
    fun setUp() {

        testUser = User(
            id = UUID.randomUUID(),
            username = "testUName",
            email = "test@mail.com",
            password = "testPassword"
        )

        val productId = UUID.randomUUID()

        testProduct = Product(
            id = productId,
            name = "testName",
            description = "TestDesc",
            imageUrl = "https://imgBase:/testImg.com",
            price = 10
        )

        orderDto = OrderDto(
            mutableListOf(ItemDto(productId, 3))
        )
        lenient().`when`(productRepository.findById(productId)).thenReturn(Optional.of(testProduct))

    }

    @Test
    fun testOrderMapperShouldContainsCorrectData() {
        val order = orderMapper.mapToOrder(orderDto, testUser)

        assertEquals(testUser, order.user)
        assertEquals(1, order.products.size)
        assertEquals(30, order.totalCost)
    }

    @Test
    fun testThrowsExceptionForInvalidProductId() {
        val invalidProductId = UUID.randomUUID()
        val invalidOrderDto = OrderDto(mutableListOf(ItemDto(invalidProductId, 1)))

        `when`(productRepository.findById(invalidProductId)).thenReturn(Optional.empty())

        assertFailsWith<IllegalArgumentException> {
            orderMapper.mapToOrder(invalidOrderDto, testUser)
        }
    }
}