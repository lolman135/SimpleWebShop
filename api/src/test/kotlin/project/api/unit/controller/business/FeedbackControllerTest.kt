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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import project.api.controller.business.FeedbackController
import project.api.dto.request.business.FeedbackDtoRequest
import project.api.dto.response.business.FeedbackDtoResponse
import project.api.dto.response.business.subDto.ProductSubDto
import project.api.dto.response.business.subDto.UserSubDto
import project.api.entity.Role
import project.api.security.CustomUserDetails
import project.api.security.JwtAuthFilter
import project.api.security.JwtTokenProvider
import project.api.service.business.feedback.FeedbackService
import java.util.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(FeedbackController::class)
@AutoConfigureMockMvc(addFilters = false)
class FeedbackControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper
) {

    @MockitoBean
    private lateinit var feedbackService: FeedbackService
    @MockitoBean
    private lateinit var jwtAuthFilter: JwtAuthFilter
    @MockitoBean
    private lateinit var jwtTokenProvider: JwtTokenProvider
    @MockitoBean
    private lateinit var securityContext: SecurityContext

    private lateinit var feedbackId: UUID
    private lateinit var feedbackRequest: FeedbackDtoRequest
    private lateinit var feedbackResponse: FeedbackDtoResponse
    private lateinit var updatedFeedbackRequest: FeedbackDtoRequest
    private lateinit var updatedFeedbackResponse: FeedbackDtoResponse
    private lateinit var userDetails: CustomUserDetails

    @BeforeEach
    fun setUp() {
        feedbackId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val productId = UUID.randomUUID()
        val roleId = UUID.randomUUID()
        userDetails = CustomUserDetails(
            user = project.api.entity.User(
                id = userId,
                username = "testUser",
                email = "user@test.com",
                password = "pass",
                roles = mutableSetOf(Role(roleId, "ROLE_USER"))
            )
        )
        feedbackRequest = FeedbackDtoRequest("Great product", 5, productId)
        feedbackResponse = FeedbackDtoResponse(
            id = feedbackId,
            review = "Great product",
            rate = 5,
            user = UserSubDto(userId, "testUser", "user@test.com"),
            product = ProductSubDto(productId, "Burger", 100)
        )

        updatedFeedbackRequest = FeedbackDtoRequest("Good product", 4, productId)
        updatedFeedbackResponse = FeedbackDtoResponse(
            id = feedbackId,
            review = "Good product",
            rate = 4,
            user = UserSubDto(userDetails.user.id!!, userDetails.username, "user@test.com"),
            product = ProductSubDto(productId, "Burger", 100)
        )

        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        SecurityContextHolder.getContext().authentication = authentication
    }

    @Test
    fun leaveFeedbackReturns200AndSavedFeedback() {
        given(feedbackService.save(feedbackRequest, userDetails.user)).willReturn(feedbackResponse)

        mockMvc.perform(
            post("/api/v1/feedbacks")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(feedbackRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(feedbackId.toString()))
            .andExpect(jsonPath("$.review").value("Great product"))
            .andExpect(jsonPath("$.rate").value(5))
            .andExpect(jsonPath("$.user.username").value("testUser"))
            .andExpect(jsonPath("$.product.name").value("Burger"))

        verify(feedbackService).save(feedbackRequest, userDetails.user)
    }

    @Test
    fun getFeedbackByIdReturnsFeedback() {
        given(feedbackService.findById(feedbackId)).willReturn(feedbackResponse)

        mockMvc.perform(get("/api/v1/feedbacks/{id}", feedbackId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(feedbackId.toString()))
            .andExpect(jsonPath("$.review").value("Great product"))

        verify(feedbackService).findById(feedbackId)
    }

    @Test
    fun getAllFeedbacksReturnsList() {
        given(feedbackService.findAll()).willReturn(listOf(feedbackResponse))

        mockMvc.perform(get("/api/v1/feedbacks"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(feedbackId.toString()))
            .andExpect(jsonPath("$[0].review").value("Great product"))

        verify(feedbackService).findAll()
    }

    @Test
    fun updateFeedbackByIdReturnsUpdatedFeedback() {
        given(feedbackService.updateById(feedbackId, updatedFeedbackRequest, userDetails.user)).willReturn(updatedFeedbackResponse)

        mockMvc.perform(
            put("/api/v1/feedbacks/{id}", feedbackId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updatedFeedbackRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(feedbackId.toString()))
            .andExpect(jsonPath("$.review").value("Good product"))
            .andExpect(jsonPath("$.rate").value(4))

        verify(feedbackService).updateById(feedbackId, updatedFeedbackRequest, userDetails.user)
    }

    @Test
    fun deleteFeedbackByIdReturnsOk() {
        mockMvc.perform(delete("/api/v1/feedbacks/{id}", feedbackId))
            .andExpect(status().isNoContent)

        verify(feedbackService).deleteById(feedbackId)
    }
}
