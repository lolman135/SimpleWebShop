package project.api.unit.mappers

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import project.api.dto.request.business.UserDtoUpdateRequest
import project.api.entity.*
import project.api.mapper.business.user.UserMapperImpl
import project.api.repository.feedback.FeedbackRepository
import project.api.repository.order.OrderRepository
import project.api.repository.role.RoleRepository
import kotlin.test.assertEquals
import java.util.*
import kotlin.test.assertFailsWith

@ExtendWith(MockitoExtension::class)
class UserMapperTest {

    @Mock private lateinit var orderRepository: OrderRepository
    @Mock private lateinit var roleRepository: RoleRepository
    @Mock private lateinit var feedbackRepository: FeedbackRepository

    @InjectMocks private lateinit var userMapper: UserMapperImpl

    private lateinit var userDtoUpdateRequest: UserDtoUpdateRequest
    private lateinit var roleId: UUID
    private lateinit var orderId: UUID
    private lateinit var feedbackId: UUID
    private lateinit var testRole: Role
    private lateinit var testOrder: Order
    private lateinit var testFeedback: Feedback
    private lateinit var testProduct1: Product
    private lateinit var testProduct2: Product
    private lateinit var testCategory: Category

    @BeforeEach
    fun setUp(){

        testCategory = Category(
            id = UUID.randomUUID(),
            name = "Test category"
        )

        userDtoUpdateRequest = UserDtoUpdateRequest(
            username = "test_username",
            password = "testPassword123",
            email = "testuser123@email.com"
        )

        roleId = UUID.randomUUID()
        orderId = UUID.randomUUID()
        feedbackId = UUID.randomUUID()

        testRole = Role(
            id = roleId,
            name = "TEST_USER"
        )

        testProduct1 = Product(
            id = UUID.randomUUID(),
            name = "Test name",
            price = 300,
            description = "Test description",
            imageUrl = "https://imgBase:/testImg.com",
            category = testCategory
        )

        testProduct2 = Product(
            id = UUID.randomUUID(),
            name = "Test name 2",
            price = 200,
            description = "Test description 2",
            imageUrl = "https://imgBase:/testImg2.com",
            category = testCategory
        )

        val testUser = User(
            id = UUID.randomUUID(),
            username = "test_user",
            email = "test_user@email.com",
            password = "testPassword"
        )

        testOrder = Order(
            id = orderId,
            totalCost = 440,
            products = mutableSetOf(
                testProduct1,
                testProduct2
            ),
            user = testUser
        )

        testFeedback = Feedback(
            id = feedbackId,
            review = "Test review",
            rate = 4,
            product = testProduct1,
            user = testUser
        )
    }

    @Test
    fun toDtoShouldMapCorrectly() {
        val userId = UUID.randomUUID()
        val role = Role(id = UUID.randomUUID(), name = "USER_ROLE")
        val order = testOrder
        val feedback = testFeedback

        val user = User(
            id = userId,
            username = "dtoUser",
            email = "dto@mail.com",
            password = "pass",
            roles = mutableSetOf(role),
            orders = mutableSetOf(order),
            feedbacks = mutableSetOf(feedback)
        )

        val dto = userMapper.toDto(user)

        assertEquals(userId, dto.id)
        assertEquals("dtoUser", dto.username)
        assertEquals("dto@mail.com", dto.email)

        assertEquals(1, dto.roles.size)
        assertEquals("USER_ROLE", dto.roles.first())

        assertEquals(1, dto.orders.size)
        val orderDto = dto.orders.first()
        assertEquals(order.id, orderDto.id)
        assertEquals(order.totalCost, orderDto.totalCost)
        assertEquals(order.products.map { it.id }, orderDto.productIds)

        assertEquals(1, dto.feedbacks.size)
        val feedbackDto = dto.feedbacks.first()
        assertEquals(feedback.id, feedbackDto.id)
        assertEquals(feedback.review, feedbackDto.review)
        assertEquals(feedback.rate, feedbackDto.rate)
    }

    @Test
    fun toDtoShouldThrowExceptionWhenIdIsNull() {
        val user = User(
            id = null,
            username = "noIdUser",
            email = "noid@mail.com",
            password = "123"
        )

        assertFailsWith<IllegalArgumentException> {
            userMapper.toDto(user)
        }
    }
}