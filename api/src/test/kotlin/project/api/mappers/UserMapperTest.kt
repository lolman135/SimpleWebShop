package project.api.mappers

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import project.api.dto.UserDto
import project.api.entity.Feedback
import project.api.entity.Order
import project.api.entity.Role
import project.api.mapper.user.UserMapperImpl
import project.api.repository.feedback.FeedbackRepository
import project.api.repository.order.OrderRepository
import project.api.repository.role.RoleRepository
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
        //TODO: finish
        testOrder = Order(
            id = orderId,
            totalCost = 440
        )
    }

    @Test
    fun testUserMapperShouldReturnNewUserWithValidData(){
        val user = userMapper.toUser(userDto)

        assertEquals("test_username", user.username)
        assertEquals("testPassword123", user.password)
        assertEquals("testuser123@email.com", user.email)

        assertTrue(user.orders.isEmpty())
        assertTrue(user.feedbacks.isEmpty())
        assertTrue(user.roles.isEmpty())
    }

    //TODO: add new tests

}