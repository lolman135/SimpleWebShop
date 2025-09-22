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
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import project.api.controller.business.CategoryController
import project.api.dto.request.business.CategoryDtoRequest
import project.api.dto.response.business.CategoryDtoResponse
import project.api.security.JwtAuthFilter
import project.api.security.JwtTokenProvider
import project.api.service.business.category.CategoryService
import java.util.*


@ExtendWith(SpringExtension::class)
@WebMvcTest(CategoryController::class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper
) {

    @MockitoBean
    private lateinit var categoryService: CategoryService
    @MockitoBean
    private lateinit var jwtAuthFilter: JwtAuthFilter
    @MockitoBean
    private lateinit var jwtTokenProvider: JwtTokenProvider

    private lateinit var categoryId: UUID
    private lateinit var categoryRequest: CategoryDtoRequest
    private lateinit var categoryResponse: CategoryDtoResponse
    private lateinit var updatedCategoryRequest: CategoryDtoRequest
    private lateinit var updatedCategoryResponse: CategoryDtoResponse

    @BeforeEach
    fun setUp() {
        categoryId = UUID.randomUUID()
        categoryRequest = CategoryDtoRequest("Drinks")
        categoryResponse = CategoryDtoResponse(categoryId, "Drinks")
        updatedCategoryRequest = CategoryDtoRequest("Hot Drinks")
        updatedCategoryResponse = CategoryDtoResponse(categoryId, "Hot Drinks")
    }

    @Test
    fun addCategoryReturns201AndSavedCategory() {
        given(categoryService.save(categoryRequest)).willReturn(categoryResponse)

        mockMvc.perform(
            post("/api/v1/categories")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(categoryRequest))
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "/categories/$categoryId"))
            .andExpect(jsonPath("$.id").value(categoryId.toString()))
            .andExpect(jsonPath("$.name").value("Drinks"))

        verify(categoryService).save(categoryRequest)
    }

    @Test
    fun getCategoryByIdReturnsCategory() {
        given(categoryService.findById(categoryId)).willReturn(categoryResponse)

        mockMvc.perform(get("/api/v1/categories/{id}", categoryId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(categoryId.toString()))
            .andExpect(jsonPath("$.name").value("Drinks"))

        verify(categoryService).findById(categoryId)
    }

    @Test
    fun getAllCategoriesReturnsList() {
        given(categoryService.findAll()).willReturn(listOf(categoryResponse))

        mockMvc.perform(get("/api/v1/categories"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(categoryId.toString()))
            .andExpect(jsonPath("$[0].name").value("Drinks"))

        verify(categoryService).findAll()
    }

    @Test
    fun updateCategoryByIdReturnsUpdatedCategory() {
        given(categoryService.updateById(categoryId, updatedCategoryRequest)).willReturn(updatedCategoryResponse)

        mockMvc.perform(
            put("/api/v1/categories/{id}", categoryId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updatedCategoryRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(categoryId.toString()))
            .andExpect(jsonPath("$.name").value("Hot Drinks"))

        verify(categoryService).updateById(categoryId, updatedCategoryRequest)
    }

    @Test
    fun deleteCategoryByIdReturnsOk() {
        mockMvc.perform(delete("/api/v1/categories/{id}", categoryId))
            .andExpect(status().isNoContent)

        verify(categoryService).deleteById(categoryId)
    }
}