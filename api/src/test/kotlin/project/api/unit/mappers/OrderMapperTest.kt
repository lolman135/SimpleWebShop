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
import project.api.entity.Product
import project.api.entity.User
import project.api.mapper.business.order.OrderMapperImpl
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
    private lateinit var orderDto: OrderDtoRequest
    private lateinit var productId: UUID
    private lateinit var category: Category

    @BeforeEach
    fun setUp() {
        productId = UUID.randomUUID()
        category = Category(
            id = UUID.randomUUID(),
            name = "Test category" // описание можно не указывать
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
            category = category // добавляем категорию
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
}
