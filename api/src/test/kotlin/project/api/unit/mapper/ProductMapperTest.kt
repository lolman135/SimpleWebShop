package project.api.unit.mapper

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import project.api.dto.request.business.ProductDtoRequest
import project.api.mapper.business.product.ProductMapperImpl
import project.api.repository.feedback.FeedbackRepository
import kotlin.test.Test
import project.api.entity.Category
import project.api.entity.Feedback
import project.api.entity.Product
import project.api.entity.User
import project.api.repository.category.CategoryRepository
import java.util.*
import kotlin.test.assertFailsWith


@ExtendWith(MockitoExtension::class)
class ProductMapperTest {

    @Mock private lateinit var feedbackRepository: FeedbackRepository
    @Mock private lateinit var categoryRepository: CategoryRepository

    @InjectMocks private lateinit var productMapper: ProductMapperImpl

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
        lenient().`when`(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category))
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

    @Test
    fun toDtoShouldMapCorrectly() {
        val productId = UUID.randomUUID()
        val feedbackId = UUID.randomUUID()
        val testFeedback = Feedback(
            id = feedbackId,
            review = "Good product",
            rate = 5,
            user = User(
                id = UUID.randomUUID(),
                username = "tester",
                email = "tester@email.com",
                password = "1234"
            ),
            product = Product(
                id = productId,
                name = "dummy",
                price = 10,
                imageUrl = "url",
                description = "desc",
                category = category
            )
        )

        val product = Product(
            id = productId,
            name = "Test product",
            description = "Test description",
            price = 30,
            imageUrl = "https://imgBase:/testImg.com",
            category = category,
            feedbacks = mutableSetOf(testFeedback)
        )

        val dto = productMapper.toDto(product)

        assertEquals(productId, dto.id)
        assertEquals("Test product", dto.name)
        assertEquals(30, dto.price)
        assertEquals("https://imgBase:/testImg.com", dto.imageUrl)
        assertEquals("Test description", dto.description)
        assertEquals(category.name, dto.category)

        assertEquals(1, dto.feedbacks.size)
        val feedbackDto = dto.feedbacks.first()
        assertEquals(feedbackId, feedbackDto.id)
        assertEquals("Good product", feedbackDto.review)
        assertEquals(5, feedbackDto.rate)
    }

    @Test
    fun toDtoShouldThrowExceptionWhenIdIsNull() {
        val product = Product(
            id = null,
            name = "No id",
            description = "desc",
            price = 10,
            imageUrl = "url",
            category = category
        )

        assertFailsWith<IllegalArgumentException> {
            productMapper.toDto(product)
        }
    }

}
