package project.api.integration.business

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import project.api.dto.request.business.CategoryDtoRequest
import project.api.entity.Category
import project.api.repository.category.CategoryRepository
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CategoryControllerIntegrationTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    val categoryRepository: CategoryRepository
) {

    private lateinit var existingCategory: Category

    @BeforeEach
    fun setUp() {
        categoryRepository.deleteAll()
        existingCategory = categoryRepository.save(Category(name = "FastFood"))
    }

    @Test
    fun getAllCategoriesReturnsList() {
        mockMvc.get("/api/v1/categories")
            .andExpect {
                status { isOk() }
                jsonPath("$[0].id") { exists() }
                jsonPath("$[0].name") { value(existingCategory.name) }
            }
    }

    @Test
    fun getCategoryByIdReturnsCategory() {
        mockMvc.get("/api/v1/categories/${existingCategory.id}")
            .andExpect {
                status { isOk() }
                jsonPath("$.id") { value(existingCategory.id.toString()) }
                jsonPath("$.name") { value(existingCategory.name) }
            }
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun addCategoryCreatesCategory() {
        val request = CategoryDtoRequest("Beverages")

        val result = mockMvc.post("/api/v1/categories") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
        }.andReturn()

        val responseJson = result.response.contentAsString
        assert(responseJson.contains("Beverages"))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun updateCategoryByIdUpdatesCategory() {
        val request = CategoryDtoRequest("Snacks")

        mockMvc.put("/api/v1/categories/${existingCategory.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
        }

        val updated = categoryRepository.findById(existingCategory.id!!).get()
        assertEquals("Snacks", updated.name)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun deleteCategoryByIdDeletesCategory() {
        mockMvc.delete("/api/v1/categories/${existingCategory.id!!}")
            .andExpect { status { isNoContent() } }

        val exists = categoryRepository.existsById(existingCategory.id!!)
        assertEquals(false, exists)
    }
}
