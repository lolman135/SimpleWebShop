package project.api.integration.business

import TestCacheConfig
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import project.api.config.TestSecurityConfig
import project.api.dto.request.business.FeedbackDtoRequest
import project.api.entity.Category
import project.api.entity.Feedback
import project.api.entity.Product
import project.api.entity.User
import project.api.repository.category.CategoryRepository
import project.api.repository.feedback.FeedbackRepository
import project.api.repository.product.ProductRepository
import project.api.repository.user.UserRepository
import project.api.security.CustomUserDetails

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig::class, TestCacheConfig::class)
class FeedbackControllerIntegrationTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    val feedbackRepository: FeedbackRepository,
    val userRepository: UserRepository,
    val productRepository: ProductRepository,
    val categoryRepository: CategoryRepository
) {
    private lateinit var existingUser: User
    private lateinit var existingCategory: Category
    private lateinit var existingProduct: Product
    private lateinit var existingFeedback: Feedback

    @BeforeEach
    fun setUp() {
        feedbackRepository.deleteAll()
        productRepository.deleteAll()
        categoryRepository.deleteAll()
        userRepository.deleteAll()

        existingUser = userRepository.save(
            User(
                username = "testuser",
                email = "testuser@example.com",
                password = "password"
            )
        )

        existingCategory = categoryRepository.save(
            Category(name = "Beverages")
        )

        existingProduct = productRepository.save(
            Product(
                name = "CocaCola",
                description = "Refreshing drink",
                price = 20,
                imageUrl = "coca.png",
                category = existingCategory
            )
        )

        existingFeedback = feedbackRepository.save(
            Feedback(
                review = "Great drink!",
                rate = 5,
                user = existingUser,
                product = existingProduct
            )
        )

        val userDetails = CustomUserDetails(existingUser)
        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        SecurityContextHolder.getContext().authentication = authentication
    }

    @Test
    @WithMockUser(username = "testuser", roles = ["ADMIN"])
    fun leaveFeedbackCreatesFeedback() {
        val request = FeedbackDtoRequest(
            review = "Nice product",
            rate = 4,
            productId = existingProduct.id!!
        )

        mockMvc.post("/api/v1/feedbacks") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.review") { value("Nice product") }
            jsonPath("$.rate") { value(4) }
            jsonPath("$.user.username") { value("testuser") }
            jsonPath("$.product.name") { value("CocaCola") }
        }
    }

    @Test
    @WithMockUser(username = "testuser", roles = ["ADMIN"])
    fun getFeedbackByIdReturnsFeedback() {
        mockMvc.get("/api/v1/feedbacks/${existingFeedback.id}")
            .andExpect {
                status { isOk() }
                jsonPath("$.review") { value(existingFeedback.review) }
                jsonPath("$.user.username") { value("testuser") }
                jsonPath("$.product.name") { value("CocaCola") }
            }
    }

    @Test
    @WithMockUser(username = "testuser", roles = ["ADMIN"])
    fun getAllFeedbacksReturnsFeedbacks() {
        mockMvc.get("/api/v1/feedbacks")
            .andExpect {
                status { isOk() }
                jsonPath("$[0].review") { value(existingFeedback.review) }
                jsonPath("$[0].user.username") { value("testuser") }
                jsonPath("$[0].product.name") { value("CocaCola") }
            }
    }

    @Test
    @WithMockUser(username = "testuser", roles = ["ADMIN"])
    fun updateFeedbackByIdUpdatesFeedback() {
        val request = FeedbackDtoRequest(
            review = "Updated review",
            rate = 3,
            productId = existingProduct.id!!
        )

        mockMvc.put("/api/v1/feedbacks/${existingFeedback.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.review") { value("Updated review") }
            jsonPath("$.rate") { value(3) }
        }
    }

    @Test
    @WithMockUser(username = "testuser", roles = ["ADMIN"])
    fun deleteFeedbackByIdDeletesFeedback() {
        mockMvc.delete("/api/v1/feedbacks/${existingFeedback.id}")
            .andExpect {
                status { isNoContent() }
            }
    }
}
