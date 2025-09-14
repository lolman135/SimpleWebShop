package project.api.unit.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import project.api.dto.request.business.CategoryDtoRequest
import project.api.dto.request.business.ProductDtoRequest
import project.api.dto.response.business.ProductDtoResponse
import project.api.dto.response.business.subDto.FeedbackSubDto
import project.api.entity.Category
import project.api.entity.Product
import project.api.exception.EntityNotFoundException
import project.api.mapper.business.category.CategoryMapper
import project.api.mapper.business.product.ProductMapper
import project.api.repository.category.CategoryRepository
import project.api.repository.product.ProductRepository
import project.api.service.business.prodcut.ProductServiceImpl
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class ProductServiceTest {

    @Mock private lateinit var productRepository: ProductRepository
    @Mock private lateinit var productMapper: ProductMapper
    @Mock private lateinit var categoryMapper: CategoryMapper
    @Mock private lateinit var categoryRepository: CategoryRepository


    @InjectMocks private lateinit var productService: ProductServiceImpl

    private lateinit var productId: UUID
    private lateinit var categoryId: UUID
    private lateinit var category: Category
    private lateinit var productDtoRequest: ProductDtoRequest
    private lateinit var product: Product
    private lateinit var categoryDtoRequest: CategoryDtoRequest
    private lateinit var categoryName: String


    private lateinit var feedbacks: List<FeedbackSubDto>
    private lateinit var productDtoResponse: ProductDtoResponse
    private lateinit var productDtoResponseList: List<ProductDtoResponse>

    @BeforeEach
    fun setUp() {
        productId = UUID.randomUUID()
        categoryId = UUID.randomUUID()
        categoryDtoRequest = CategoryDtoRequest(name = "Test category")
        categoryName = "Test Category"
        category = Category(
            id = categoryId,
            name = "Test category",
        )
        productDtoRequest = ProductDtoRequest(
            name = "Test name",
            description = "Test description",
            price = 10,
            imageUrl = "https://imgBase:/testImg.com",
            categoryId = categoryId
        )
        product = Product(
            id = productId,
            name = "Test name",
            description = "Test description",
            price = 10,
            imageUrl = "https://imgBase:/testImg.com",
            category = category
        )
        feedbacks = listOf(FeedbackSubDto(UUID.randomUUID(), "Good", 5))
        productDtoResponse = ProductDtoResponse(
            id = productId,
            name = "Test name",
            price = 10,
            imageUrl = "https://imgBase:/testImg.com",
            description = "Test description",
            feedbacks = feedbacks,
            category = category.name
        )
        productDtoResponseList = listOf(productDtoResponse)
    }

    @Test
    fun saveShouldMapToDtoAndSaveEntity() {
        `when`(productMapper.toProduct(productDtoRequest)).thenReturn(product)
        `when`(productRepository.save(product)).thenReturn(product)
        `when`(productMapper.toDto(product)).thenReturn(productDtoResponse)

        val savedProduct = productService.save(productDtoRequest)

        assertEquals(productDtoResponse, savedProduct)
        verify(productMapper).toProduct(productDtoRequest)
        verify(productRepository).save(product)
        verify(productMapper).toDto(product)
        verifyNoMoreInteractions(productMapper, productRepository)
    }

    @Test
    fun findAllShouldReturnListOfProducts() {
        `when`(productRepository.findAll()).thenReturn(listOf(product))
        `when`(productMapper.toDto(product)).thenReturn(productDtoResponse)

        val result = productService.findAll()

        assertEquals(productDtoResponseList, result)
        verify(productRepository).findAll()
        verify(productMapper).toDto(product)
        verifyNoMoreInteractions(productRepository, productMapper)
    }

    @Test
    fun findByIdShouldReturnProductIfExists() {
        `when`(productRepository.findById(productId)).thenReturn(Optional.of(product))
        `when`(productMapper.toDto(product)).thenReturn(productDtoResponse)

        val result = productService.findById(productId)

        assertEquals(productDtoResponse, result)
        verify(productRepository).findById(productId)
        verify(productMapper).toDto(product)
        verifyNoMoreInteractions(productRepository, productMapper)
    }

    @Test
    fun findByIdShouldThrowExceptionWhenProductDoesNotExist() {
        `when`(productRepository.findById(productId)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            productService.findById(productId)
        }

        verify(productRepository).findById(productId)
        verifyNoMoreInteractions(productRepository)
        verifyNoInteractions(productMapper)
    }

    @Test
    fun updateByIdShouldUpdateProductWhenExists() {
        `when`(productRepository.existsById(productId)).thenReturn(true)
        `when`(productMapper.toProduct(productDtoRequest)).thenReturn(product)
        `when`(productRepository.save(product)).thenReturn(product)
        `when`(productMapper.toDto(product)).thenReturn(productDtoResponse)

        val result = productService.updateById(productId, productDtoRequest)

        assertEquals(productDtoResponse, result)
        verify(productRepository).existsById(productId)
        verify(productMapper).toProduct(productDtoRequest)
        verify(productRepository).save(product)
        verify(productMapper).toDto(product)
        verifyNoMoreInteractions(productRepository, productMapper)
    }

    @Test
    fun updateByIdShouldThrowExceptionWhenProductDoesNotExist() {
        `when`(productRepository.existsById(productId)).thenReturn(false)

        assertThrows<EntityNotFoundException> {
            productService.updateById(productId, productDtoRequest)
        }

        verify(productRepository).existsById(productId)
        verify(productMapper, never()).toProduct(productDtoRequest)
        verify(productRepository, never()).save(product)
        verifyNoMoreInteractions(productRepository, productMapper)
    }

    @Test
    fun deleteByIdShouldDeleteProductWhenExists() {
        `when`(productRepository.existsById(productId)).thenReturn(true)

        val isDeleted = productService.deleteById(productId)

        assertTrue(isDeleted)
        verify(productRepository).existsById(productId)
        verify(productRepository).deleteById(productId)
        verifyNoMoreInteractions(productRepository)
    }

    @Test
    fun deleteByIdShouldThrowExceptionWhenProductDoesNotExist() {
        `when`(productRepository.existsById(productId)).thenReturn(false)

        assertThrows<EntityNotFoundException> {
            productService.deleteById(productId)
        }

        verify(productRepository).existsById(productId)
        verify(productRepository, never()).deleteById(productId)
        verifyNoMoreInteractions(productRepository)
    }


    @Test
    fun findProductsByCategoryShouldReturnListWhenCategoryExists() {
        `when`(categoryRepository.findCategoryByName(categoryName)).thenReturn(Optional.of(category))
        `when`(productRepository.findProductsByCategory(category)).thenReturn(listOf(product))
        `when`(productMapper.toDto(product)).thenReturn(productDtoResponse)

        val result = productService.findProductsByCategory(categoryName)

        assertEquals(productDtoResponseList, result)
        verify(categoryRepository).findCategoryByName(categoryName)
        verify(productRepository).findProductsByCategory(category)
        verify(productMapper).toDto(product)
        verifyNoMoreInteractions(categoryRepository, productRepository, productMapper)
    }

    @Test
    fun findProductsByCategoryShouldThrowExceptionWhenCategoryDoesNotExist() {
        `when`(categoryRepository.findCategoryByName(categoryName)).thenReturn(Optional.empty())

        assertFailsWith<EntityNotFoundException> {
            productService.findProductsByCategory(categoryName)
        }

        verify(categoryRepository).findCategoryByName(categoryName)
        verifyNoInteractions(productRepository, productMapper)
    }

    @Test
    fun findProductByNamePrefixShouldReturnListOfProducts() {
        val prefix = "Test"
        `when`(productRepository.findProductByNamePrefix(prefix)).thenReturn(listOf(product))
        `when`(productMapper.toDto(product)).thenReturn(productDtoResponse)

        val result = productService.findProductByNamePrefix(prefix)

        assertEquals(productDtoResponseList, result)
        verify(productRepository).findProductByNamePrefix(prefix)
        verify(productMapper).toDto(product)
        verifyNoMoreInteractions(productRepository, productMapper)
    }

    @Test
    fun findProductByNamePrefixShouldReturnEmptyListWhenNoMatches() {
        val prefix = "None"
        `when`(productRepository.findProductByNamePrefix(prefix)).thenReturn(emptyList())

        val result = productService.findProductByNamePrefix(prefix)

        assertTrue(result.isEmpty())
        verify(productRepository).findProductByNamePrefix(prefix)
        verifyNoInteractions(productMapper)
        verifyNoMoreInteractions(productRepository)
    }

}
