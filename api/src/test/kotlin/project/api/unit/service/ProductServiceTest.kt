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
import project.api.entity.Category
import project.api.entity.Product
import project.api.exception.EntityNotFoundException
import project.api.mapper.business.category.CategoryMapper
import project.api.mapper.business.product.ProductMapper
import project.api.repository.product.ProductRepository
import project.api.service.business.prodcut.ProductServiceImpl
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class ProductServiceTest {

    @Mock
    private lateinit var productRepository: ProductRepository
    @Mock
    private lateinit var productMapper: ProductMapper
    @Mock
    private lateinit var categoryMapper: CategoryMapper

    @InjectMocks
    private lateinit var productService: ProductServiceImpl

    private lateinit var productId: UUID
    private lateinit var categoryId: UUID
    private lateinit var category: Category
    private lateinit var productDtoRequest: ProductDtoRequest
    private lateinit var product: Product
    private lateinit var categoryDtoRequest: CategoryDtoRequest
    private lateinit var productList: List<Product>


    @BeforeEach
    fun setUp() {
        productId = UUID.randomUUID()
        categoryId = UUID.randomUUID()
        categoryDtoRequest = CategoryDtoRequest(name = "Test category")
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
        categoryDtoRequest = CategoryDtoRequest(name = "Test category")
        productList = listOf(product)
    }

    @Test
    fun saveShouldMapToDtoAndSaveEntity() {
        `when`(productMapper.toProduct(productDtoRequest)).thenReturn(product)
        `when`(productRepository.save(product)).thenReturn(product)

        val savedProduct = productService.save(productDtoRequest)

        assertEquals(product, savedProduct)
        verify(productMapper).toProduct(productDtoRequest)
        verify(productRepository).save(product)
        verifyNoMoreInteractions(productMapper, productRepository)
    }

    @Test
    fun findAllShouldReturnListOfProducts() {
        val products = listOf(product)
        `when`(productRepository.findAll()).thenReturn(products)

        val result = productService.findAll()

        assertEquals(products, result)
        verify(productRepository).findAll()
        verifyNoMoreInteractions(productRepository)
    }

    @Test
    fun findByIdShouldReturnProductIfExists() {
        `when`(productRepository.findById(productId)).thenReturn(Optional.of(product))

        val result = productService.findById(productId)

        assertEquals(product, result)
        verify(productRepository).findById(productId)
        verifyNoMoreInteractions(productRepository)
    }

    @Test
    fun findByIdShouldThrowExceptionWhenProductDoesNotExist() {
        `when`(productRepository.findById(productId)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            productService.findById(productId)
        }

        verify(productRepository).findById(productId)
        verifyNoMoreInteractions(productRepository)
    }

    @Test
    fun updateByIdShouldUpdateProductWhenExists() {
        `when`(productRepository.existsById(productId)).thenReturn(true)
        `when`(productMapper.toProduct(productDtoRequest)).thenReturn(product)
        `when`(productRepository.save(product)).thenReturn(product)

        val result = productService.updateById(productId, productDtoRequest)

        assertEquals(product, result)
        verify(productRepository).existsById(productId)
        verify(productMapper).toProduct(productDtoRequest)
        verify(productRepository).save(product)
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
        verify(productRepository, never()).deleteById(any())
        verifyNoMoreInteractions(productRepository)
    }

    @Test
    fun findProductsByCategoryShouldReturnListOfProducts() {
        `when`(categoryMapper.toCategory(categoryDtoRequest)).thenReturn(category)
        `when`(productRepository.findProductsByCategory(category)).thenReturn(productList)

        val result = productService.findProductsByCategory(categoryDtoRequest)

        assertEquals(productList, result)
        verify(categoryMapper).toCategory(categoryDtoRequest)
        verify(productRepository).findProductsByCategory(category)
        verifyNoMoreInteractions(categoryMapper, productRepository)
    }

    @Test
    fun findProductsByCategoryShouldReturnEmptyListWhenNoProducts() {
        `when`(categoryMapper.toCategory(categoryDtoRequest)).thenReturn(category)
        `when`(productRepository.findProductsByCategory(category)).thenReturn(emptyList())

        val result = productService.findProductsByCategory(categoryDtoRequest)

        assertTrue(result.isEmpty())
        verify(categoryMapper).toCategory(categoryDtoRequest)
        verify(productRepository).findProductsByCategory(category)
        verifyNoMoreInteractions(categoryMapper, productRepository)
    }
}