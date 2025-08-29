package project.api.unit.mappers

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import project.api.dto.UserDto
import project.api.entity.*
import project.api.mapper.user.UserMapperImpl
import project.api.repository.feedback.FeedbackRepository
import project.api.repository.order.OrderRepository
import project.api.repository.role.RoleRepository
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.mockito.Mockito.`when`
import java.util.*
import kotlin.test.assertFailsWith

@ExtendWith(MockitoExtension::class)
class UserMapperTest {

    @Mock
    private lateinit var orderRepository: OrderRepository
    @Mock
    private lateinit var roleRepository: RoleRepository
    @Mock
    private lateinit var feedbackRepository: FeedbackRepository

    @InjectMocks
    private lateinit var userMapper: UserMapperImpl

    private lateinit var userDto: UserDto
    private lateinit var roleId: UUID
    private lateinit var orderId: UUID
    private lateinit var feedbackId: UUID
    private lateinit var testRole: Role
    private lateinit var testOrder: Order
    private lateinit var testFeedback: Feedback
    private lateinit var testProduct1: Product
    private lateinit var testProduct2: Product

    @BeforeEach
    fun setUp(){
        userDto = UserDto(
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
            imageUrl = "https://imgBase:/testImg.com"
        )

        testProduct2 = Product(
            id = UUID.randomUUID(),
            name = "Test name 2",
            price = 200,
            description = "Test description 2",
            imageUrl = "https://imgBase:/testImg2.com"
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
    fun toRoleShouldReturnNewUserWithValidData(){
        val user = userMapper.toUser(userDto)

        assertEquals("test_username", user.username)
        assertEquals("testPassword123", user.password)
        assertEquals("testuser123@email.com", user.email)

        assertTrue(user.orders.isEmpty())
        assertTrue(user.feedbacks.isEmpty())
        assertTrue(user.roles.isEmpty())
    }

    @Test
    fun testUserMapperShouldReturnExistingUserWithAllDependencies(){
        `when`(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder))
        `when`(feedbackRepository.findById(feedbackId)).thenReturn(Optional.of(testFeedback))
        `when`(roleRepository.findById(roleId)).thenReturn(Optional.of(testRole))

        val existUserDto = UserDto(
            username = "test_username",
            password = "testPassword123",
            email = "testuser123@email.com",
            orderIds = listOf(orderId),
            feedbackIds = listOf(feedbackId),
            roleIds = listOf(roleId)
        )

        val user = userMapper.toUser(existUserDto)

        assertEquals("test_username", user.username)
        assertEquals("testPassword123", user.password)
        assertEquals("testuser123@email.com", user.email)

        assertTrue(user.feedbacks.isNotEmpty())
        assertEquals(orderId, user.orders.first().id)
        assertEquals("TEST_USER", user.roles.first().name)

        assertEquals("Test name", user.orders.first().products.first().name)
        verify(orderRepository).findById(orderId)
        verify(feedbackRepository).findById(feedbackId)
        verify(roleRepository).findById(roleId)
    }

    @Test
    fun testUserMapperShouldFailsByWrongOrderId(){
        val invalidOrderId = UUID.randomUUID()
        `when`(orderRepository.findById(invalidOrderId)).thenThrow(IllegalArgumentException("Wrong Id!"))

        val invalidOrderIdsUserDto = UserDto(
            username = "test_username",
            password = "testPassword123",
            email = "testuser123@email.com",
            orderIds = listOf(invalidOrderId),
        )
        assertFailsWith<IllegalArgumentException> {
            userMapper.toUser(invalidOrderIdsUserDto)
        }
        verify(orderRepository).findById(invalidOrderId)
    }

    @Test
    fun testUserMapperShouldFailsByWrongRoleId(){
        val invalidRoleId = UUID.randomUUID()
        `when`(roleRepository.findById(invalidRoleId)).thenThrow(IllegalArgumentException("Wrong Id!"))

        val invalidOrderIdsUserDto = UserDto(
            username = "test_username",
            password = "testPassword123",
            email = "testuser123@email.com",
            roleIds = listOf(invalidRoleId),
        )
        assertFailsWith<IllegalArgumentException> {
            userMapper.toUser(invalidOrderIdsUserDto)
        }
        verify(roleRepository).findById(invalidRoleId)
    }

    @Test
    fun testUserMapperShouldFailsByWrongFeedbackId(){
        val invalidFeedbackId = UUID.randomUUID()
        `when`(feedbackRepository.findById(invalidFeedbackId)).thenThrow(IllegalArgumentException("Wrong Id!"))

        val invalidOrderIdsUserDto = UserDto(
            username = "test_username",
            password = "testPassword123",
            email = "testuser123@email.com",
            feedbackIds = listOf(invalidFeedbackId),
        )
        assertFailsWith<IllegalArgumentException> {
            userMapper.toUser(invalidOrderIdsUserDto)
        }
        verify(feedbackRepository).findById(invalidFeedbackId)
    }
}