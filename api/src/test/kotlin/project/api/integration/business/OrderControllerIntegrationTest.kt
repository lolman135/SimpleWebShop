package project.api.integration.business

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
import project.api.dto.request.business.ItemDto
import project.api.dto.request.business.OrderDtoRequest
import project.api.entity.Category
import project.api.entity.Product
import project.api.entity.User
import project.api.repository.category.CategoryRepository
import project.api.repository.product.ProductRepository
import project.api.repository.user.UserRepository
import project.api.security.CustomUserDetails
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig::class)
class OrderControllerIntegrationTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    val userRepository: UserRepository,
    val productRepository: ProductRepository,
    val categoryRepository: CategoryRepository
) {

    private lateinit var existingUser: User
    private lateinit var existingCategory: Category
    private lateinit var existingProduct: Product
    private lateinit var existingOrderId: UUID

    @BeforeEach
    fun setUp() {
        // чистим всё
        userRepository.deleteAll()
        productRepository.deleteAll()
        categoryRepository.deleteAll()

        existingUser = userRepository.save(
            User(
                username = "testuser",
                email = "testuser@example.com",
                password = "password"
            )
        )

        existingCategory = categoryRepository.save(Category(name = "Beverages"))
        existingProduct = productRepository.save(
            Product(
                name = "CocaCola",
                description = "Refreshing drink",
                price = 20,
                imageUrl = "coca.png",
                category = existingCategory
            )
        )

        val userDetails = CustomUserDetails(existingUser)
        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        SecurityContextHolder.getContext().authentication = authentication
    }

    @Test
    @WithMockUser(username = "testuser", roles = ["ADMIN"])
    fun createOrderCreatesOrder() {
        val request = OrderDtoRequest(
            items = listOf(ItemDto(productId = existingProduct.id!!, productsAmount = 2))
        )

        mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.user.username") { value("testuser") }
            jsonPath("$.products[0].name") { value("CocaCola") }
            jsonPath("$.products[0].price") { value(20) }
        }
    }

    @Test
    @WithMockUser(username = "testuser", roles = ["ADMIN"])
    fun getAllOrdersReturnsOrders() {
        val orderRequest = OrderDtoRequest(
            items = listOf(ItemDto(productId = existingProduct.id!!, productsAmount = 2))
        )
        val result = mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(orderRequest)
        }.andReturn()
        existingOrderId = objectMapper.readTree(result.response.contentAsString)["id"].asText().let { UUID.fromString(it) }

        mockMvc.get("/api/v1/orders").andExpect {
            status { isOk() }
            jsonPath("$[0].user.username") { value("testuser") }
            jsonPath("$[0].products[0].name") { value("CocaCola") }
        }
    }

    @Test
    @WithMockUser(username = "testuser", roles = ["ADMIN"])
    fun getOrderByIdReturnsOrder() {
        val orderRequest = OrderDtoRequest(
            items = listOf(ItemDto(productId = existingProduct.id!!, productsAmount = 2))
        )
        val result = mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(orderRequest)
        }.andReturn()
        existingOrderId = objectMapper.readTree(result.response.contentAsString)["id"].asText().let { UUID.fromString(it) }

        mockMvc.get("/api/v1/orders/$existingOrderId").andExpect {
            status { isOk() }
            jsonPath("$.user.username") { value("testuser") }
            jsonPath("$.products[0].name") { value("CocaCola") }
        }
    }

    @Test
    @WithMockUser(username = "testuser", roles = ["ADMIN"])
    fun updateOrderByIdUpdatesOrder() {
        val orderRequest = OrderDtoRequest(
            items = listOf(ItemDto(productId = existingProduct.id!!, productsAmount = 2))
        )
        val result = mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(orderRequest)
        }.andReturn()
        existingOrderId = objectMapper.readTree(result.response.contentAsString)["id"].asText().let { UUID.fromString(it) }

        val updateRequest = OrderDtoRequest(
            items = listOf(ItemDto(productId = existingProduct.id!!, productsAmount = 3))
        )

        mockMvc.put("/api/v1/orders/$existingOrderId") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(updateRequest)
        }.andExpect {
            status { isOk() }
            jsonPath("$.products[0].price") { value(20) }
        }
    }

    @Test
    @WithMockUser(username = "testuser", roles = ["ADMIN"])
    fun deleteOrderByIdDeletesOrder() {
        val orderRequest = OrderDtoRequest(
            items = listOf(ItemDto(productId = existingProduct.id!!, productsAmount = 2))
        )
        val result = mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(orderRequest)
        }.andReturn()
        existingOrderId = objectMapper.readTree(result.response.contentAsString)["id"].asText().let { UUID.fromString(it) }

        mockMvc.delete("/api/v1/orders/$existingOrderId").andExpect {
            status { isNoContent() }
        }
    }
}
