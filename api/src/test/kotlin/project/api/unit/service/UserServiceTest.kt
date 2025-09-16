package project.api.unit.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder
import project.api.dto.request.authentication.RegisterRequest
import project.api.dto.request.business.UserDtoUpdateMeRequest
import project.api.dto.request.business.UserDtoUpdateRequest
import project.api.dto.response.business.UserDtoResponse
import project.api.dto.response.business.subDto.FeedbackSubDto
import project.api.dto.response.business.subDto.OrderSubDto
import project.api.entity.*
import project.api.exception.EntityNotFoundException
import project.api.exception.UserAlreadyExistsException
import project.api.mapper.authentication.toUser
import project.api.mapper.business.user.UserMapper
import project.api.repository.feedback.FeedbackRepository
import project.api.repository.order.OrderRepository
import project.api.repository.role.RoleRepository
import project.api.repository.user.UserRepository
import project.api.service.business.role.RoleService
import project.api.service.business.user.UserServiceImpl
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceTest {
    @Mock private lateinit var userRepository: UserRepository
    @Mock private lateinit var userMapper: UserMapper
    @Mock private lateinit var roleService: RoleService
    @Mock private lateinit var roleRepository: RoleRepository
    @Mock private lateinit var orderRepository: OrderRepository
    @Mock private lateinit var feedbackRepository: FeedbackRepository
    @Mock private lateinit var passwordEncoder: PasswordEncoder

    @InjectMocks private lateinit var userService: UserServiceImpl

    private lateinit var userId: UUID
    private lateinit var user: User
    private lateinit var defaultRole: Role
    private lateinit var order: Order
    private lateinit var feedback: Feedback
    private lateinit var product: Product
    private lateinit var updateRequest: UserDtoUpdateRequest
    private lateinit var registerRequest: RegisterRequest
    private lateinit var userDtoResponse: UserDtoResponse
    private lateinit var updateMeRequest: UserDtoUpdateMeRequest

    private lateinit var orderId: UUID
    private lateinit var feedbackId: UUID
    private lateinit var roleId: UUID
    private lateinit var productId: UUID

    @BeforeEach
    fun setUp() {
        userId = UUID.randomUUID()
        orderId = UUID.randomUUID()
        feedbackId = UUID.randomUUID()
        roleId = UUID.randomUUID()
        productId = UUID.randomUUID()

        defaultRole = Role(roleId, "ROLE_USER")
        user = User(
            id = userId,
            username = "JohnDoe",
            email = "john@example.com",
            password = "securePass123"
        )

        val category = Category(UUID.randomUUID(), "TestCategory")
        product = Product(
            id = productId,
            name = "Product1",
            price = 1000,
            imageUrl = "url",
            description = "desc",
            feedbacks = mutableSetOf(),
            category = category
        )

        order = Order(
            id = orderId,
            createdAt = LocalDateTime.now(),
            totalCost = 1500,
            user = user,
            products = mutableSetOf(product)
        )

        feedback = Feedback(
            id = feedbackId,
            review = "Great",
            rate = 5,
            user = user,
            product = product
        )

        registerRequest = RegisterRequest(
            username = "JohnDoe",
            email = "john@example.com",
            password = "securePass123"
        )

        updateRequest = UserDtoUpdateRequest(
            username = "UpdatedName",
            email = "updated@example.com",
            password = "newPass",
            roleIds = listOf(roleId),
            orderIds = listOf(orderId),
            feedbackIds = listOf(feedbackId)
        )

        updateMeRequest = UserDtoUpdateMeRequest(
            username = "UpdatedName",
            email = "updated@example.com",
            password = "newPass",
        )

        userDtoResponse = UserDtoResponse(
            id = userId,
            username = "UpdatedName",
            email = "updated@example.com",
            orders = listOf(OrderSubDto(orderId, order.totalCost, listOf(productId))),
            feedbacks = listOf(FeedbackSubDto(feedbackId, feedback.review!!, feedback.rate)),
            roles = listOf("ROLE_USER")
        )
    }

    @Test
    fun saveShouldSaveUserWhenUnique() {
        val userEntity = registerRequest.toUser()
        userEntity.roles = mutableSetOf(defaultRole)

        `when`(userRepository.existsUserByUsername(registerRequest.username)).thenReturn(false)
        `when`(userRepository.existsUserByEmail(registerRequest.email)).thenReturn(false)
        `when`(roleService.getDefaultRole()).thenReturn(defaultRole)
        `when`(userRepository.save(userEntity)).thenReturn(userEntity)

        val result = userService.save(registerRequest)

        assertEquals(userEntity.username, result.username)
        assertEquals(userEntity.email, result.email)
        assertEquals(userEntity.password, result.password)
        assertTrue(result.roles.contains(defaultRole))

        verify(userRepository).existsUserByUsername(registerRequest.username)
        verify(userRepository).existsUserByEmail(registerRequest.email)
        verify(roleService).getDefaultRole()
        verify(userRepository).save(userEntity)
    }

    @Test
    fun saveShouldThrowWhenUsernameExists() {
        `when`(userRepository.existsUserByUsername(registerRequest.username)).thenReturn(true)

        assertThrows<UserAlreadyExistsException> { userService.save(registerRequest) }

        verify(userRepository).existsUserByUsername(registerRequest.username)
        verify(userRepository, never()).existsUserByEmail(registerRequest.email)
        verify(roleService, never()).getDefaultRole()
        verify(userRepository, never()).save(user)
    }

    @Test
    fun saveShouldThrowWhenEmailExists() {
        `when`(userRepository.existsUserByUsername(registerRequest.username)).thenReturn(false)
        `when`(userRepository.existsUserByEmail(registerRequest.email)).thenReturn(true)

        assertThrows<UserAlreadyExistsException> { userService.save(registerRequest) }

        verify(userRepository).existsUserByUsername(registerRequest.username)
        verify(userRepository).existsUserByEmail(registerRequest.email)
        verify(roleService, never()).getDefaultRole()
        verify(userRepository, never()).save(user)
    }

    @Test
    fun findAllShouldReturnUsers() {
        `when`(userRepository.findAll()).thenReturn(listOf(user))
        `when`(userMapper.toDto(user)).thenReturn(userDtoResponse)

        val result = userService.findAll()

        assertEquals(listOf(userDtoResponse), result)
        verify(userRepository).findAll()
        verify(userMapper).toDto(user)
    }

    @Test
    fun findByIdShouldReturnUser() {
        `when`(userRepository.findById(userId)).thenReturn(Optional.of(user))
        `when`(userMapper.toDto(user)).thenReturn(userDtoResponse)

        val result = userService.findById(userId)

        assertEquals(userDtoResponse, result)
        verify(userRepository).findById(userId)
        verify(userMapper).toDto(user)
    }

    @Test
    fun findByIdShouldThrowWhenNotFound() {
        `when`(userRepository.findById(userId)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> { userService.findById(userId) }

        verify(userRepository).findById(userId)
        verify(userMapper, never()).toDto(user)
    }

    @Test
    fun updateByIdShouldUpdateAllFields() {
        `when`(userRepository.findById(userId)).thenReturn(Optional.of(user))
        `when`(roleRepository.findById(roleId)).thenReturn(Optional.of(defaultRole))
        `when`(orderRepository.findById(orderId)).thenReturn(Optional.of(order))
        `when`(feedbackRepository.findById(feedbackId)).thenReturn(Optional.of(feedback))
        `when`(passwordEncoder.encode("newPass")).thenReturn("hashedPass")
        `when`(userRepository.save(user)).thenReturn(user)
        `when`(userMapper.toDto(user)).thenReturn(userDtoResponse)

        val result = userService.updateById(userId, updateRequest)

        assertEquals(userDtoResponse, result)
        assertEquals("UpdatedName", user.username)
        assertEquals("updated@example.com", user.email)
        assertEquals("hashedPass", user.password)
        assertTrue(user.roles.contains(defaultRole))
        assertTrue(user.orders.contains(order))
        assertTrue(user.feedbacks.contains(feedback))

        verify(userRepository).findById(userId)
        verify(roleRepository).findById(roleId)
        verify(orderRepository).findById(orderId)
        verify(feedbackRepository).findById(feedbackId)
        verify(passwordEncoder).encode("newPass")
        verify(userRepository).save(user)
        verify(userMapper).toDto(user)
    }

    @Test
    fun updateByIdShouldThrowWhenUserNotFound() {
        `when`(userRepository.findById(userId)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> { userService.updateById(userId, updateRequest) }

        verify(userRepository).findById(userId)
        verify(userRepository, never()).save(user)
        verify(userMapper, never()).toDto(user)
    }

    @Test
    fun updateMeByIdShouldShouldUpdateAllFields(){
        `when`(userRepository.findById(userId)).thenReturn(Optional.of(user))
        `when`(passwordEncoder.encode("newPass")).thenReturn("hashedPass")
        `when`(userRepository.save(user)).thenReturn(user)
        `when`(userMapper.toDto(user)).thenReturn(userDtoResponse)

        val result = userService.updateMeById(userId, updateMeRequest)

        assertEquals(userDtoResponse, result)
        assertEquals("UpdatedName", user.username)
        assertEquals("updated@example.com", user.email)
        assertEquals("hashedPass", user.password)

        verify(userRepository).findById(userId)
        verify(passwordEncoder).encode("newPass")
        verify(userRepository).save(user)
        verify(userMapper).toDto(user)
    }

    @Test
    fun updateMeByIdShouldThrowWhenUserNotFound() {
        `when`(userRepository.findById(userId)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> { userService.updateMeById(userId, updateMeRequest) }

        verify(userRepository).findById(userId)
        verify(userRepository, never()).save(user)
        verify(userMapper, never()).toDto(user)
    }

    @Test
    fun deleteByIdShouldDeleteUser() {
        `when`(userRepository.existsById(userId)).thenReturn(true)

        val result = userService.deleteById(userId)

        assertTrue(result)
        verify(userRepository).existsById(userId)
        verify(userRepository).deleteById(userId)
    }

    @Test
    fun deleteByIdShouldThrowWhenNotFound() {
        `when`(userRepository.existsById(userId)).thenReturn(false)

        assertThrows<EntityNotFoundException> { userService.deleteById(userId) }

        verify(userRepository).existsById(userId)
        verify(userRepository, never()).deleteById(userId)
    }
}
