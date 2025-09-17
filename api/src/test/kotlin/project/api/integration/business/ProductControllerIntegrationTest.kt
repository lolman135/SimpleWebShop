package project.api.integration.business

import TestCacheConfig
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import project.api.dto.request.business.CategoryDtoRequest
import project.api.dto.request.business.ProductDtoRequest
import project.api.entity.Category
import project.api.entity.Product
import project.api.repository.category.CategoryRepository
import project.api.repository.order.OrderRepository
import project.api.repository.product.ProductRepository


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Import(TestCacheConfig::class)
class ProductControllerIntegrationTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    val productRepository: ProductRepository,
    val categoryRepository: CategoryRepository,
    val orderRepository: OrderRepository
) {
    private lateinit var existingCategory: Category
    private lateinit var existingProduct: Product

    @BeforeEach
    fun setUp() {
        orderRepository.deleteAll()
        productRepository.deleteAll()
        categoryRepository.deleteAll()

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
    }

    @Test
    fun getAllProductsReturnsProducts() {
        mockMvc.get("/api/v1/products")
            .andExpect {
                status { isOk() }
                jsonPath("$[0].name") { value(existingProduct.name) }
            }
    }

    @Test
    fun getProductByIdReturnsProduct() {
        mockMvc.get("/api/v1/products/${existingProduct.id}")
            .andExpect {
                status { isOk() }
                jsonPath("$.name") { value(existingProduct.name) }
            }
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun getProductsByNamePrefixReturnsProducts() {

        mockMvc.get("/api/v1/products/name") {
            param("prefix", "Co")
        }.andExpect {
            status { isOk() }
            jsonPath("$[0].name") { value(existingProduct.name) }
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun getProductsByCategoryReturnsProducts() {
        mockMvc.get("/api/v1/products/category") {
            param("name", existingCategory.name)
        }.andExpect {
            status { isOk() }
            jsonPath("$[0].name") { value(existingProduct.name) }
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun addProductCreatesProduct() {
        val request = ProductDtoRequest(
            name = "Pepsi",
            description = "Cola drink",
            price = 18,
            imageUrl = "pepsi.png",
            categoryId = existingCategory.id!!
        )

        mockMvc.post("/api/v1/products") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.name") { value("Pepsi") }
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun updateProductByIdUpdatesProduct() {
        val request = ProductDtoRequest(
            name = "CocaCola Updated",
            description = "Updated description",
            price = 22,
            imageUrl = "coca_updated.png",
            categoryId = existingCategory.id!!
        )

        mockMvc.put("/api/v1/products/${existingProduct.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.name") { value("CocaCola Updated") }
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun deleteProductByIdDeletesProduct() {
        mockMvc.delete("/api/v1/products/${existingProduct.id}")
            .andExpect {
                status { isNoContent() }
            }
    }
}
