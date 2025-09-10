package project.api.unit.mappers

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import project.api.dto.request.business.FeedbackDtoRequest
import project.api.entity.Product
import project.api.entity.User
import project.api.repository.product.ProductRepository
import java.util.*
import org.mockito.Mockito.`when`
import project.api.entity.Category
import project.api.entity.Feedback
import project.api.mapper.business.feedback.FeedbackMapperImpl
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


@ExtendWith(MockitoExtension::class)
class FeedbackMapperTest {

    @Mock private lateinit var productRepository: ProductRepository

    @InjectMocks private lateinit var feedbackMapper: FeedbackMapperImpl

    private lateinit var testUser: User
    private lateinit var testProduct: Product
    private lateinit var feedbackDtoRequest: FeedbackDtoRequest
    private lateinit var productId: UUID
    private lateinit var userId: UUID
    private lateinit var testCategory: Category

    @BeforeEach
    fun setUp() {
        userId = UUID.randomUUID()
        testCategory = Category(UUID.randomUUID(), "Test category")

        testUser = User(
            id = userId,
            username = "testUName",
            email = "test@mail.com",
            password = "testPassword"
        )

        productId = UUID.randomUUID()

        testProduct = Product(
            id = productId,
            name = "testName",
            description = "TestDesc",
            imageUrl = "https://imgBase:/testImg.com",
            price = 15,
            category = testCategory
        )

        feedbackDtoRequest = FeedbackDtoRequest(
            rate = 4,
            productId = productId,
            review = "test review"
        )
    }

    @Test
    fun toFeedbackShouldReturnCorrectData(){
        `when`(productRepository.findById(productId)).thenReturn(Optional.of(testProduct))

        val feedback = feedbackMapper.toFeedback(feedbackDtoRequest, testUser)

        assertEquals(testProduct, feedback.product)
        assertEquals(4, feedback.rate)
        assertEquals("test review", feedback.review)
        assertEquals(userId, feedback.user.id)
        verify(productRepository).findById(productId)
    }

    @Test
    fun toFeedbackShouldThrowException(){
        `when`(productRepository.findById(productId)).thenThrow(IllegalArgumentException("Wrong id provided"))

        assertFailsWith<IllegalArgumentException> {
            val feedback = feedbackMapper.toFeedback(feedbackDtoRequest, testUser)
        }
        verify(productRepository).findById(productId)
    }

    @Test
    fun toFeedbackReviewShouldBeNull(){
        val feedbackDtoRequestWithoutReview = FeedbackDtoRequest(
            rate = 4,
            productId = productId
        )

        `when`(productRepository.findById(productId)).thenReturn(Optional.of(testProduct))

        val feedback = feedbackMapper.toFeedback(feedbackDtoRequestWithoutReview, testUser)

        assertNull(feedback.review)
        verify(productRepository).findById(productId)
    }

    @Test
    fun toDtoShouldMapCorrectly() {
        val feedbackId = UUID.randomUUID()
        val feedback = Feedback(
            id = feedbackId,
            review = "Great product!",
            rate = 5,
            user = testUser,
            product = testProduct
        )

        val dto = feedbackMapper.toDto(feedback)

        assertEquals(feedbackId, dto.id)
        assertEquals("Great product!", dto.review)
        assertEquals(5, dto.rate)

        // user subdto
        assertEquals(testUser.id, dto.user.id)
        assertEquals(testUser.username, dto.user.username)
        assertEquals(testUser.email, dto.user.email)

        // product subdto
        assertEquals(testProduct.id, dto.product.id)
        assertEquals(testProduct.name, dto.product.name)
        assertEquals(testProduct.price, dto.product.price)
    }

    @Test
    fun toDtoShouldThrowExceptionWhenIdIsNull() {
        val feedback = Feedback(
            id = null,
            review = "No id",
            rate = 3,
            user = testUser,
            product = testProduct
        )

        val exception = assertFailsWith<IllegalArgumentException> {
            feedbackMapper.toDto(feedback)
        }
        assertEquals("Id is not provided", exception.message)
    }


}