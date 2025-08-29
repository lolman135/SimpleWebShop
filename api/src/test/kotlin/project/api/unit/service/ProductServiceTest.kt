package project.api.unit.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import project.api.dto.business.ProductDto
import project.api.entity.Product
import project.api.exception.EntityNotFoundException
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

    @InjectMocks
    private lateinit var productService: ProductServiceImpl

    private lateinit var productId: UUID
    private lateinit var productDto: ProductDto
    private lateinit var product: Product

    @BeforeEach
    fun setUp() {
        productId = UUID.randomUUID()
        productDto = ProductDto(
            name = "Test name",
            description = "Test description",
            price = 10,
            imageUrl = "https://imgBase:/testImg.com"
        )
        product = Product(
            id = productId,
            name = "Test name",
            description = "Test description",
            price = 10,
            imageUrl = "https://imgBase:/testImg.com"
        )
    }

    @Test
    fun saveShouldMapToDtoAndSaveEntity() {
        `when`(productMapper.toProduct(productDto)).thenReturn(product)
        `when`(productRepository.save(product)).thenReturn(product)

        val savedProduct = productService.save(productDto)
        assertEquals(product, savedProduct)
        verify(productMapper).toProduct(productDto)
        verify(productRepository).save(product)
    }

    @Test
    fun findAllShouldReturnListOfProducts() {
        val products = listOf(product)
        `when`(productRepository.findAll()).thenReturn(products)

        val result = productService.findAll()

        assertEquals(products, result)
        verify(productRepository).findAll()
    }

    @Test
    fun findByIdShouldReturnProductIfExists() {
        `when`(productRepository.findById(productId)).thenReturn(Optional.of(product))

        val result = productService.findById(productId)

        assertEquals(product, result)
        verify(productRepository).findById(productId)
    }

    @Test
    fun findByIdShouldThrowExceptionWhenProductDoesNotExist() {
        `when`(productRepository.findById(productId)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            productService.findById(productId)
        }
        verify(productRepository).findById(productId)
    }

    @Test
    fun updateByIdShouldUpdateUserWhenExists(){
        `when`(productRepository.existsById(productId)).thenReturn(true)
        `when`(productMapper.toProduct(productDto)).thenReturn(product)
        `when`(productRepository.save(product)).thenReturn(product)

        val result = productService.updateById(productId, productDto)

        assertEquals(product, result)
        verify(productRepository).existsById(productId)
        verify(productMapper).toProduct(productDto)
        verify(productRepository).save(product)
    }

    @Test
    fun updateByIdShouldThrowExceptionWhenProductDoesNotExists(){
        `when`(productRepository.existsById(productId)).thenReturn(false)

        assertThrows<EntityNotFoundException> {
            productService.updateById(productId, productDto)
        }
        verify(productRepository).existsById(productId)
        verify(productMapper, never()).toProduct(productDto)
        verify(productRepository, never()).save(product)
    }

    @Test
    fun deleteByIdShouldDeleteProductWhenExists(){
        `when`(productRepository.existsById(productId)).thenReturn(true)

        val isDeleted = productService.deleteById(productId)

        assertTrue(isDeleted)
        verify(productRepository).existsById(productId)
        verify(productRepository).deleteById(productId)
    }

    @Test
    fun deleteByIdShouldThrowExceptionWhenProductDoesNotExists(){
        `when`(productRepository.existsById(productId)).thenReturn(false)

        assertThrows<EntityNotFoundException> {
            productService.deleteById(productId)
        }

        verify(productRepository).existsById(productId)
        verify(productRepository, never()).deleteById(productId)
    }
}