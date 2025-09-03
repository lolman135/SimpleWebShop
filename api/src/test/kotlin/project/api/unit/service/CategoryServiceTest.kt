package project.api.unit.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import project.api.dto.business.CategoryDto
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

    @Mock
    private lateinit var categoryRepository: CategoryRepository
    @Mock
    private lateinit var categoryMapper: CategoryMapper

    @InjectMocks
    private lateinit var categoryService: CategoryServiceImpl

    private lateinit var categoryId: UUID
    private lateinit var categoryDto: CategoryDto
    private lateinit var category: Category
    private lateinit var categoryList: List<Category>

    @BeforeEach
    fun setUp() {
        categoryId = UUID.randomUUID()
        categoryDto = CategoryDto(name = "Test Category")
        category = Category(id = categoryId, name = "Test Category")
        categoryList = listOf(category)
    }

    @Test
    fun saveShouldMapDtoAndSaveCategory() {
        `when`(categoryMapper.toCategory(categoryDto)).thenReturn(category)
        `when`(categoryRepository.save(category)).thenReturn(category)

        val result = categoryService.save(categoryDto)

        assertEquals(category, result)
        verify(categoryMapper).toCategory(categoryDto)
        verify(categoryRepository).save(category)
        verifyNoMoreInteractions(categoryMapper, categoryRepository)
    }

    @Test
    fun findAllShouldReturnListOfCategories() {
        `when`(categoryRepository.findAll()).thenReturn(categoryList)

        val result = categoryService.findAll()

        assertEquals(categoryList, result)
        verify(categoryRepository).findAll()
        verifyNoMoreInteractions(categoryRepository)
    }

    @Test
    fun findByIdShouldReturnCategoryIfExists() {
        `when`(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category))

        val result = categoryService.findById(categoryId)

        assertEquals(category, result)
        verify(categoryRepository).findById(categoryId)
        verifyNoMoreInteractions(categoryRepository)
    }

    @Test
    fun findByIdShouldThrowExceptionIfNotFound() {
        `when`(categoryRepository.findById(categoryId)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            categoryService.findById(categoryId)
        }

        verify(categoryRepository).findById(categoryId)
        verifyNoMoreInteractions(categoryRepository)
    }

    @Test
    fun updateByIdShouldUpdateCategoryWhenExists() {
        `when`(categoryRepository.existsById(categoryId)).thenReturn(true)
        `when`(categoryMapper.toCategory(categoryDto)).thenReturn(category)
        `when`(categoryRepository.save(category)).thenReturn(category)

        val result = categoryService.updateById(categoryId, categoryDto)

        assertEquals(category, result)
        assertEquals(categoryId, category.id)
        verify(categoryRepository).existsById(categoryId)
        verify(categoryMapper).toCategory(categoryDto)
        verify(categoryRepository).save(category)
        verifyNoMoreInteractions(categoryRepository, categoryMapper)
    }

    @Test
    fun updateByIdShouldThrowExceptionIfCategoryDoesNotExist() {
        `when`(categoryRepository.existsById(categoryId)).thenReturn(false)

        assertThrows<EntityNotFoundException> {
            categoryService.updateById(categoryId, categoryDto)
        }

        verify(categoryRepository).existsById(categoryId)
        verify(categoryMapper, never()).toCategory(categoryDto)
        verify(categoryRepository, never()).save(category)
        verifyNoMoreInteractions(categoryRepository, categoryMapper)
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
