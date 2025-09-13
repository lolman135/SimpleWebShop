package project.api.unit.controller.business


import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import project.api.controller.business.FeedbackController
import project.api.controller.business.OrderController
import project.api.dto.request.business.ItemDto
import project.api.dto.request.business.OrderDtoRequest
import project.api.dto.response.business.OrderDtoResponse
import project.api.dto.response.business.subDto.UserSubDto
import project.api.entity.Role
import project.api.entity.User
import project.api.security.CustomUserDetails
import project.api.security.JwtAuthFilter
import project.api.security.JwtTokenProvider
import project.api.service.business.feedback.FeedbackService
import project.api.service.business.order.OrderService
import project.api.service.business.user.UserService
import java.time.LocalDateTime
import java.util.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(OrderController::class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper
) {
    @MockitoBean
    private lateinit var orderService: OrderService
    @MockitoBean
    private lateinit var userService: UserService
    @MockitoBean
    private lateinit var jwtAuthFilter: JwtAuthFilter
    @MockitoBean
    private lateinit var jwtTokenProvider: JwtTokenProvider
    @MockitoBean
    private lateinit var securityContext: SecurityContext

    private lateinit var orderId: UUID
    private lateinit var request: OrderDtoRequest
    private lateinit var response: OrderDtoResponse
    private lateinit var updatedOrderRequest: OrderDtoRequest
    private lateinit var updatedOrderResponse: OrderDtoRequest
    private lateinit var userDetails: CustomUserDetails

    @BeforeEach
    fun setUp(){

        val userId = UUID.randomUUID()
        userDetails = CustomUserDetails(
            User(
                id = userId,
                username = "testUser",
                email = "user@test.com",
                password = "pass",
                roles = mutableSetOf(Role(UUID.randomUUID(), "ROLE_USER"))
            )
        )

        orderId = UUID.randomUUID()
        request = OrderDtoRequest(listOf(ItemDto(UUID.randomUUID(), 2)))
        response = OrderDtoResponse(
            id = orderId,
            createdAt = LocalDateTime.now(),
            totalCost = 200,
            products = listOf(),
            user = UserSubDto(
                id = userDetails.user.id!!,
                username = userDetails.username,
                email = "user@test.com"
            )
        )
        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        SecurityContextHolder.getContext().authentication = authentication
    }

    @Test
    fun createOrderReturns200AndSavedOrder() {
        given(orderService.save(request, userDetails.user)).willReturn(response)

        mockMvc.perform(
            post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(orderId.toString()))
            .andExpect(jsonPath("$.user.username").value("testUser"))

        verify(orderService).save(request, userDetails.user)
    }

    @Test
    fun getOrderByIdReturnsOrder() {
        given(orderService.findById(orderId)).willReturn(response)

        mockMvc.perform(get("/api/v1/orders/{id}", orderId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(orderId.toString()))

        verify(orderService).findById(orderId)
    }

    @Test
    fun getAllOrdersReturnsList() {
        given(orderService.findAll()).willReturn(listOf(response))

        mockMvc.perform(get("/api/v1/orders"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(orderId.toString()))

        verify(orderService).findAll()
    }

    @Test
    fun updateOrderByIdReturnsUpdatedOrder() {
        val updatedRequest = OrderDtoRequest(listOf(ItemDto(UUID.randomUUID(), 3)))
        val updatedResponse = response.copy(totalCost = 300)

        given(orderService.updateById(orderId, updatedRequest, userDetails.user)).willReturn(updatedResponse)

        mockMvc.perform(
            put("/api/v1/orders/{id}", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalCost").value(300))

        verify(orderService).updateById(orderId, updatedRequest, userDetails.user)
    }

    @Test
    fun deleteOrderByIdReturnsNoContent() {
        mockMvc.perform(delete("/api/v1/orders/{id}", orderId))
            .andExpect(status().isNoContent)

        verify(orderService).deleteById(orderId)
    }

    @Test
    fun getAllProductsForUserReturnsUserOrders() {
        val rawUser = userDetails.user
        val ordersForUser = listOf(response)

        given(userService.findRawUserById(userDetails.getId()!!)).willReturn(rawUser)
        given(orderService.findAllForUser(rawUser)).willReturn(ordersForUser)

        mockMvc.perform(get("/api/v1/orders/my"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(orderId.toString()))
            .andExpect(jsonPath("$[0].user.username").value("testUser"))

        verify(userService).findRawUserById(userDetails.getId()!!)
        verify(orderService).findAllForUser(rawUser)
    }
}