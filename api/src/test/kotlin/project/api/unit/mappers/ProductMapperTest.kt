package project.api.unit.mappers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import project.api.dto.request.business.ProductDtoRequest
import project.api.mapper.business.product.ProductMapperImpl
import project.api.repository.feedback.FeedbackRepository
import kotlin.test.Test
import org.mockito.Mockito.`when`
import project.api.entity.Category
import project.api.repository.category.CategoryRepository
import java.util.*
import kotlin.test.assertFailsWith


@ExtendWith(MockitoExtension::class)
class ProductMapperTest {

    @Mock
    private lateinit var feedbackRepository: FeedbackRepository
    @Mock
    private lateinit var categoryRepository: CategoryRepository

    @InjectMocks
    private lateinit var productMapper: ProductMapperImpl

    private lateinit var productDtoRequest: ProductDtoRequest
    private lateinit var categoryId: UUID
    private lateinit var category: Category

    @BeforeEach
    fun setUp(){
        categoryId = UUID.randomUUID()
        productDtoRequest = ProductDtoRequest(
            name = "Test product",
            description = "Test description",
            price = 30,
            imageUrl = "https://imgBase:/testImg.com",
            categoryId = categoryId
        )
        category = Category(categoryId, "Test category")
        `when`(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category))
    }

    @Test
    fun toProductShouldCreateProductWithCorrectData(){
        val product = productMapper.toProduct(productDtoRequest)

        assertEquals("Test product", product.name)
        assertEquals("Test description", product.description)
        assertEquals(30, product.price)
        assertEquals("https://imgBase:/testImg.com", product.imageUrl)
        assertTrue(product.feedbacks.isEmpty())
    }

    @Test
    fun toProductShouldCreateProductWithoutFeedbacks(){
        val product = productMapper.toProduct(productDtoRequest)
        assertTrue(product.feedbacks.isEmpty())
    }

    @Test
    fun toProductShouldFailsWithIllegalArgumentException(){
        `when`(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category))
        val invalidFeedbackId = UUID.randomUUID()
        val invalidProductDtoRequest = ProductDtoRequest(
            name = "Test product",
            description = "Test description",
            price = 30,
            imageUrl = "https://imgBase:/testImg.com",
            feedbackIds = mutableListOf(invalidFeedbackId),
            categoryId = categoryId
        )
        `when`(feedbackRepository.findById(invalidFeedbackId)).thenThrow(IllegalArgumentException("Wrong id provided"))

        assertFailsWith<IllegalArgumentException> {
            productMapper.toProduct(invalidProductDtoRequest)
        }
        verify(feedbackRepository).findById(invalidFeedbackId)
    }

    @Test
    fun toProductShouldCreateProductWithCorrectCategory() {
        `when`(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category))

        val product = productMapper.toProduct(productDtoRequest)

        assertEquals(category, product.category)
        verify(categoryRepository).findById(categoryId)
    }

    @Test
    fun toProductShouldFailWhenCategoryNotFound() {
        `when`(categoryRepository.findById(categoryId)).thenReturn(Optional.empty())

        assertFailsWith<IllegalArgumentException> {
            productMapper.toProduct(productDtoRequest)
        }
        verify(categoryRepository).findById(categoryId)
    }

    @Test
    fun toProductShouldCreateProductWithCategoryAndWithoutFeedbacks() {
        `when`(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category))

        val product = productMapper.toProduct(productDtoRequest)

        assertEquals(category, product.category)
        assertTrue(product.feedbacks.isEmpty())
        verify(categoryRepository).findById(categoryId)
    }
}
