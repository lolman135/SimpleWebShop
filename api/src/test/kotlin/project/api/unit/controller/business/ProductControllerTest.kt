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
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import project.api.controller.business.ProductController
import project.api.dto.request.business.CategoryDtoRequest
import project.api.dto.request.business.ProductDtoRequest
import project.api.dto.response.business.ProductDtoResponse
import project.api.security.JwtAuthFilter
import project.api.security.JwtTokenProvider
import project.api.service.business.prodcut.ProductService
import java.util.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(ProductController::class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper
) {

    @MockitoBean
    private lateinit var productService: ProductService
    @MockitoBean
    private lateinit var jwtAuthFilter: JwtAuthFilter
    @MockitoBean
    private lateinit var jwtTokenProvider: JwtTokenProvider

    private lateinit var productId: UUID
    private lateinit var categoryId: UUID
    private lateinit var productRequest: ProductDtoRequest
    private lateinit var productResponse: ProductDtoResponse
    private lateinit var updatedProductRequest: ProductDtoRequest
    private lateinit var updatedProductResponse: ProductDtoResponse
    private lateinit var categoryRequest: CategoryDtoRequest

    @BeforeEach
    fun setUp() {
        productId = UUID.randomUUID()
        categoryId = UUID.randomUUID()

        productRequest = ProductDtoRequest(
            name = "Burger",
            description = "Tasty burger",
            price = 100,
            imageUrl = "http://image.url",
            feedbackIds = listOf(),
            categoryId = categoryId
        )

        productResponse = ProductDtoResponse(
            id = productId,
            name = "Burger",
            price = 100,
            imageUrl = "http://image.url",
            description = "Tasty burger",
            feedbacks = listOf(),
            category = "Fast Food"
        )

        updatedProductRequest = ProductDtoRequest(
            name = "Cheese Burger",
            description = "Tasty cheese burger",
            price = 120,
            imageUrl = "http://image.url/cheese",
            feedbackIds = listOf(),
            categoryId = categoryId
        )

        updatedProductResponse = ProductDtoResponse(
            id = productId,
            name = "Cheese Burger",
            price = 120,
            imageUrl = "http://image.url/cheese",
            description = "Tasty cheese burger",
            feedbacks = listOf(),
            category = "Fast Food"
        )

        categoryRequest = CategoryDtoRequest("Fast food")
    }

    @Test
    fun addProductReturns201AndSavedProduct() {
        given(productService.save(productRequest)).willReturn(productResponse)

        mockMvc.perform(
            post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest))
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "/products/$productId"))
            .andExpect(jsonPath("$.id").value(productId.toString()))
            .andExpect(jsonPath("$.name").value("Burger"))
            .andExpect(jsonPath("$.price").value(100))

        verify(productService).save(productRequest)
    }

    @Test
    fun getProductByIdReturnsProduct() {
        given(productService.findById(productId)).willReturn(productResponse)

        mockMvc.perform(get("/api/v1/products/{id}", productId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(productId.toString()))
            .andExpect(jsonPath("$.name").value("Burger"))

        verify(productService).findById(productId)
    }

    @Test
    fun getAllProductsReturnsList() {
        given(productService.findAll()).willReturn(listOf(productResponse))

        mockMvc.perform(get("/api/v1/products"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(productId.toString()))
            .andExpect(jsonPath("$[0].name").value("Burger"))

        verify(productService).findAll()
    }

    @Test
    fun getProductsByCategoryReturnsList() {
        val categoryName = "Fast Food"
        given(productService.findProductsByCategory(categoryName)).willReturn(listOf(productResponse))

        mockMvc.perform(
            get("/api/v1/products/category")
                .param("name", categoryName)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(productId.toString()))
            .andExpect(jsonPath("$[0].name").value("Burger"))

        verify(productService).findProductsByCategory(categoryName)
    }

    @Test
    fun updateProductByIdReturnsUpdatedProduct() {
        given(productService.updateById(productId, updatedProductRequest)).willReturn(updatedProductResponse)

        mockMvc.perform(
            put("/api/v1/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProductRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(productId.toString()))
            .andExpect(jsonPath("$.name").value("Cheese Burger"))

        verify(productService).updateById(productId, updatedProductRequest)
    }

    @Test
    fun deleteProductByIdReturnsOk() {
        mockMvc.perform(delete("/api/v1/products/{id}", productId))
            .andExpect(status().isNoContent)

        verify(productService).deleteById(productId)
    }

    @Test
    fun getProductsByNamePrefixReturnsList() {
        val prefix = "Bur"
        given(productService.findProductByNamePrefix(prefix)).willReturn(listOf(productResponse))

        mockMvc.perform(get("/api/v1/products/name")
            .param("prefix", prefix))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("Burger"))

        verify(productService).findProductByNamePrefix(prefix)
    }
}
