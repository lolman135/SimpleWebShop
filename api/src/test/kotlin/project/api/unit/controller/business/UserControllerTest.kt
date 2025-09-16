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
import project.api.controller.business.UserController
import project.api.dto.request.business.UserDtoUpdateRequest
import project.api.dto.response.business.UserDtoResponse
import project.api.dto.response.business.subDto.FeedbackSubDto
import project.api.dto.response.business.subDto.OrderSubDto
import project.api.entity.Role
import project.api.entity.User
import project.api.security.CustomUserDetails
import project.api.security.JwtAuthFilter
import project.api.security.JwtTokenProvider
import project.api.service.business.user.UserService
import java.util.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(UserController::class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper
) {

    @MockitoBean
    private lateinit var userService: UserService
    @MockitoBean
    private lateinit var jwtAuthFilter: JwtAuthFilter
    @MockitoBean
    private lateinit var jwtTokenProvider: JwtTokenProvider
    @MockitoBean
    private lateinit var securityContext: SecurityContext

    private lateinit var userDetails: CustomUserDetails
    private lateinit var userId: UUID
    private lateinit var userResponse: UserDtoResponse
    private lateinit var updateRequest: UserDtoUpdateRequest

    @BeforeEach
    fun setUp() {
        userId = UUID.randomUUID()
        val user = User(
            id = userId,
            username = "testUser",
            email = "user@test.com",
            password = "pass",
            roles = mutableSetOf(Role(UUID.randomUUID(), "ROLE_USER"))
        )
        userDetails = CustomUserDetails(user)

        userResponse = UserDtoResponse(
            id = userId,
            username = "testUser",
            email = "user@test.com",
            orders = listOf(OrderSubDto(UUID.randomUUID(), 200, listOf(UUID.randomUUID()))),
            feedbacks = listOf(FeedbackSubDto(UUID.randomUUID(), "Good", 5)),
            roles = listOf("ROLE_USER")
        )

        updateRequest = UserDtoUpdateRequest(username = "updatedUser", email = "updated@test.com")

        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        SecurityContextHolder.getContext().authentication = authentication
    }

    @Test
    fun getUserByIdReturnsUser() {
        given(userService.findById(userId)).willReturn(userResponse)

        mockMvc.perform(get("/api/v1/users/{id}", userId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(userId.toString()))
            .andExpect(jsonPath("$.username").value("testUser"))

        verify(userService).findById(userId)
    }

    @Test
    fun getMeReturnsCurrentUser() {
        given(userService.findById(userId)).willReturn(userResponse)

        mockMvc.perform(get("/api/v1/users/me"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(userId.toString()))
            .andExpect(jsonPath("$.username").value("testUser"))

        verify(userService).findById(userId)
    }

    @Test
    fun getUsersByUsernamePrefixReturnsList() {
        val prefix = "test"
        given(userService.findAllUsersByUsernamePrefix(prefix)).willReturn(listOf(userResponse))

        mockMvc.perform(get("/api/v1/users/username?prefix=$prefix"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].username").value("testUser"))

        verify(userService).findAllUsersByUsernamePrefix(prefix)
    }

    @Test
    fun getAllUsersReturnsList() {
        given(userService.findAll()).willReturn(listOf(userResponse))

        mockMvc.perform(get("/api/v1/users"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(userId.toString()))

        verify(userService).findAll()
    }

    @Test
    fun updateUserByIdReturnsUpdatedUser() {
        given(userService.updateById(userId, updateRequest)).willReturn(userResponse.copy(username = "updatedUser"))

        mockMvc.perform(
            patch("/api/v1/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.username").value("updatedUser"))

        verify(userService).updateById(userId, updateRequest)
    }

    @Test
    fun updateMeReturnsUpdatedUser() {
        given(userService.updateById(userId, updateRequest)).willReturn(userResponse.copy(username = "updatedUser"))

        mockMvc.perform(
            patch("/api/v1/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.username").value("updatedUser"))

        verify(userService).updateById(userId, updateRequest)
    }

    @Test
    fun deleteUserByIdReturnsNoContent() {
        mockMvc.perform(delete("/api/v1/users/{id}", userId))
            .andExpect(status().isNoContent)

        verify(userService).deleteById(userId)
    }

    @Test
    fun deleteMeReturnsNoContent() {
        mockMvc.perform(delete("/api/v1/users/me"))
            .andExpect(status().isNoContent)

        verify(userService).deleteById(userId)
    }
}
