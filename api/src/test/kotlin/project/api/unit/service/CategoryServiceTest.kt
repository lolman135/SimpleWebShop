package project.api.unit.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import project.api.dto.request.business.CategoryDtoRequest
import project.api.dto.response.business.CategoryDtoResponse
import project.api.entity.Category
import project.api.exception.EntityNotFoundException
import project.api.mapper.business.category.CategoryMapper
import project.api.repository.category.CategoryRepository
import project.api.service.business.category.CategoryServiceImpl
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class CategoryServiceTest {

    @Mock private lateinit var categoryRepository: CategoryRepository
    @Mock private lateinit var categoryMapper: CategoryMapper

    @InjectMocks private lateinit var categoryService: CategoryServiceImpl

    private lateinit var categoryId: UUID
    private lateinit var categoryDtoRequest: CategoryDtoRequest
    private lateinit var category: Category
    private lateinit var categoryDtoResponse: CategoryDtoResponse
    private lateinit var categoryList: List<Category>

    @BeforeEach
    fun setUp() {
        categoryId = UUID.randomUUID()
        categoryDtoRequest = CategoryDtoRequest(name = "Test Category")
        category = Category(id = categoryId, name = "Test Category")
        categoryDtoResponse = CategoryDtoResponse(id = categoryId, name = "Test Category")
        categoryList = listOf(category)
    }

    @Test
    fun saveShouldMapDtoAndSaveCategory() {
        `when`(categoryMapper.toCategory(categoryDtoRequest)).thenReturn(category)
        `when`(categoryRepository.save(category)).thenReturn(category)
        `when`(categoryMapper.toDto(category)).thenReturn(categoryDtoResponse)

        val result = categoryService.save(categoryDtoRequest)

        assertEquals(categoryDtoResponse, result)
        verify(categoryMapper).toCategory(categoryDtoRequest)
        verify(categoryRepository).save(category)
        verify(categoryMapper).toDto(category)
        verifyNoMoreInteractions(categoryMapper, categoryRepository)
    }

    @Test
    fun findAllShouldReturnListOfCategoryDtoResponse() {
        `when`(categoryRepository.findAll()).thenReturn(categoryList)
        `when`(categoryMapper.toDto(category)).thenReturn(categoryDtoResponse)

        val result = categoryService.findAll()

        assertEquals(listOf(categoryDtoResponse), result)
        verify(categoryRepository).findAll()
        verify(categoryMapper).toDto(category)
        verifyNoMoreInteractions(categoryRepository, categoryMapper)
    }

    @Test
    fun findByIdShouldReturnCategoryDtoResponseIfExists() {
        `when`(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category))
        `when`(categoryMapper.toDto(category)).thenReturn(categoryDtoResponse)

        val result = categoryService.findById(categoryId)

        assertEquals(categoryDtoResponse, result)
        verify(categoryRepository).findById(categoryId)
        verify(categoryMapper).toDto(category)
        verifyNoMoreInteractions(categoryRepository, categoryMapper)
    }

    @Test
    fun findByIdShouldThrowExceptionIfNotFound() {
        `when`(categoryRepository.findById(categoryId)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            categoryService.findById(categoryId)
        }

        verify(categoryRepository).findById(categoryId)
        verifyNoMoreInteractions(categoryRepository)
        verifyNoInteractions(categoryMapper)
    }

    @Test
    fun updateByIdShouldUpdateCategoryWhenExists() {
        `when`(categoryRepository.existsById(categoryId)).thenReturn(true)
        `when`(categoryMapper.toCategory(categoryDtoRequest)).thenReturn(category)
        `when`(categoryRepository.save(category)).thenReturn(category)
        `when`(categoryMapper.toDto(category)).thenReturn(categoryDtoResponse)

        val result = categoryService.updateById(categoryId, categoryDtoRequest)

        assertEquals(categoryDtoResponse, result)
        assertEquals(categoryId, category.id)
        verify(categoryRepository).existsById(categoryId)
        verify(categoryMapper).toCategory(categoryDtoRequest)
        verify(categoryRepository).save(category)
        verify(categoryMapper).toDto(category)
        verifyNoMoreInteractions(categoryRepository, categoryMapper)
    }

    @Test
    fun updateByIdShouldThrowExceptionIfCategoryDoesNotExist() {
        `when`(categoryRepository.existsById(categoryId)).thenReturn(false)

        assertThrows<EntityNotFoundException> {
            categoryService.updateById(categoryId, categoryDtoRequest)
        }

        verify(categoryRepository).existsById(categoryId)
        verifyNoInteractions(categoryMapper)
        verify(categoryRepository, never()).save(category)
    }

    @Test
    fun deleteByIdShouldDeleteCategoryWhenExists() {
        `when`(categoryRepository.existsById(categoryId)).thenReturn(true)

        val result = categoryService.deleteById(categoryId)

        assertTrue(result)
        verify(categoryRepository).existsById(categoryId)
        verify(categoryRepository).deleteById(categoryId)
        verifyNoMoreInteractions(categoryRepository)
    }

    @Test
    fun deleteByIdShouldThrowExceptionIfCategoryDoesNotExist() {
        `when`(categoryRepository.existsById(categoryId)).thenReturn(false)

        assertThrows<EntityNotFoundException> {
            categoryService.deleteById(categoryId)
        }

        verify(categoryRepository).existsById(categoryId)
        verify(categoryRepository, never()).deleteById(any())
        verifyNoMoreInteractions(categoryRepository)
    }
}
