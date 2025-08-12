package project.api.mappers

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import project.api.dto.FeedbackDto
import project.api.entity.Product
import project.api.entity.User
import project.api.repository.product.ProductRepository
import java.util.*
import org.mockito.Mockito.`when`
import project.api.mapper.feedback.FeedbackMapperImpl
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


@ExtendWith(MockitoExtension::class)
class FeedbackMapperTest {

    @Mock
    private lateinit var productRepository: ProductRepository

    @InjectMocks
    private lateinit var feedbackMapper: FeedbackMapperImpl

    private lateinit var testUser: User
    private lateinit var testProduct: Product
    private lateinit var feedbackDto: FeedbackDto
    private lateinit var productId: UUID
    private lateinit var userId: UUID

    @BeforeEach
    fun setUp() {
        userId = UUID.randomUUID()

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
            price = 15
        )

        feedbackDto = FeedbackDto(
            rate = 4,
            productId = productId,
            userId = userId,
            review = "test review"
        )
    }

    @Test
    fun testFeedbackMapperShouldReturnCorrectData(){
        `when`(productRepository.findById(productId)).thenReturn(Optional.of(testProduct))

        val feedback = feedbackMapper.mapToFeedback(feedbackDto, testUser)

        assertEquals(testProduct, feedback.product)
        assertEquals(4, feedback.rate)
        assertEquals("test review", feedback.review)
        assertEquals(userId, feedback.user.id)
    }

    @Test
    fun testFeedbackMapperShouldThrowException(){
        val notExistedProductId = UUID.randomUUID()
        val invalidFeedbackDto = FeedbackDto (
            rate = 4,
            productId = notExistedProductId,
            userId = userId,
            review = "test review"
        )

        `when`(productRepository.findById(notExistedProductId)).thenThrow(IllegalArgumentException("Wrong id provided"))

        assertFailsWith<IllegalArgumentException> {
            val feedback = feedbackMapper.mapToFeedback(invalidFeedbackDto, testUser)
        }
    }

    @Test
    fun testFeedbackMapperReviewShouldBeNull(){
        val feedbackDtoWithoutReview = FeedbackDto(
            rate = 4,
            userId = userId,
            productId = productId
        )

        `when`(productRepository.findById(productId)).thenReturn(Optional.of(testProduct))

        val feedback = feedbackMapper.mapToFeedback(feedbackDtoWithoutReview, testUser)

        assertNull(feedback.review)
    }

}