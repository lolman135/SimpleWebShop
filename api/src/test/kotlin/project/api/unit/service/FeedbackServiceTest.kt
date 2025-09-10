package project.api.unit.service

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import project.api.dto.request.business.FeedbackDtoRequest
import project.api.dto.response.business.FeedbackDtoResponse
import project.api.dto.response.business.subDto.ProductSubDto
import project.api.dto.response.business.subDto.UserSubDto
import project.api.entity.*
import project.api.exception.EntityNotFoundException
import project.api.mapper.business.feedback.FeedbackMapper
import project.api.repository.feedback.FeedbackRepository
import project.api.service.business.feedback.FeedbackServiceImpl
import java.util.*
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class FeedbackServiceTest {

    @Mock private lateinit var feedbackMapper: FeedbackMapper
    @Mock private lateinit var feedbackRepository: FeedbackRepository

    @InjectMocks private lateinit var feedbackService: FeedbackServiceImpl

    private lateinit var feedbackId: UUID
    private lateinit var userId: UUID
    private lateinit var productId: UUID
    private lateinit var categoryId: UUID
    private lateinit var feedbackDtoRequest: FeedbackDtoRequest
    private lateinit var user: User
    private lateinit var category: Category
    private lateinit var product: Product
    private lateinit var feedback: Feedback
    private lateinit var feedbackDtoResponse: FeedbackDtoResponse

    @BeforeEach
    fun setUp() {
        feedbackId = UUID.randomUUID()
        productId = UUID.randomUUID()
        userId = UUID.randomUUID()
        categoryId = UUID.randomUUID()

        category = Category(id = categoryId, name = "Test category")

        feedbackDtoRequest = FeedbackDtoRequest(
            productId = productId,
            rate = 5,
            review = "Test review"
        )

        user = User(
            id = userId,
            username = "JohnDoe",
            email = "john@example.com",
            password = "securePass123"
        )

        product = Product(
            id = productId,
            name = "Test name",
            description = "Test description",
            price = 10,
            imageUrl = "https://imgBase:/testImg.com",
            category = category
        )

        feedback = Feedback(
            id = feedbackId,
            user = user,
            product = product,
            rate = 5,
            review = "Test review"
        )

        feedbackDtoResponse = FeedbackDtoResponse(
            id = feedbackId,
            review = "Test review",
            rate = 5,
            user = UserSubDto(user.id!!, user.username, user.email),
            product = ProductSubDto(product.id!!, product.name, product.price)
        )
    }

    @Test
    fun saveShouldMapDtoToFeedbackAndSaveEntity() {
        `when`(feedbackMapper.toFeedback(feedbackDtoRequest, user)).thenReturn(feedback)
        `when`(feedbackRepository.save(feedback)).thenReturn(feedback)
        `when`(feedbackMapper.toDto(feedback)).thenReturn(feedbackDtoResponse)

        val result = feedbackService.save(feedbackDtoRequest, user)

        assertEquals(feedbackDtoResponse, result)
        verify(feedbackMapper).toFeedback(feedbackDtoRequest, user)
        verify(feedbackRepository).save(feedback)
        verify(feedbackMapper).toDto(feedback)
    }

    @Test
    fun findAllShouldReturnListOfFeedbackDtoResponses() {
        val feedbacks = listOf(feedback)
        `when`(feedbackRepository.findAll()).thenReturn(feedbacks)
        `when`(feedbackMapper.toDto(feedback)).thenReturn(feedbackDtoResponse)

        val result = feedbackService.findAll()

        assertEquals(listOf(feedbackDtoResponse), result)
        verify(feedbackRepository).findAll()
        verify(feedbackMapper).toDto(feedback)
    }

    @Test
    fun findByIdShouldReturnFeedbackDtoResponseIfExists() {
        `when`(feedbackRepository.findById(feedbackId)).thenReturn(Optional.of(feedback))
        `when`(feedbackMapper.toDto(feedback)).thenReturn(feedbackDtoResponse)

        val result = feedbackService.findById(feedbackId)

        assertEquals(feedbackDtoResponse, result)
        verify(feedbackRepository).findById(feedbackId)
        verify(feedbackMapper).toDto(feedback)
    }

    @Test
    fun findByIdShouldThrowExceptionWhenFeedbackDoesNotExist() {
        `when`(feedbackRepository.findById(feedbackId)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            feedbackService.findById(feedbackId)
        }

        verify(feedbackRepository).findById(feedbackId)
        verifyNoInteractions(feedbackMapper)
    }

    @Test
    fun updateByIdShouldUpdateFeedbackWhenExists() {
        `when`(feedbackRepository.existsById(feedbackId)).thenReturn(true)
        `when`(feedbackMapper.toFeedback(feedbackDtoRequest, user)).thenReturn(feedback)
        `when`(feedbackRepository.save(feedback)).thenReturn(feedback)
        `when`(feedbackMapper.toDto(feedback)).thenReturn(feedbackDtoResponse)

        val result = feedbackService.updateById(feedbackId, feedbackDtoRequest, user)

        assertEquals(feedbackDtoResponse, result)
        verify(feedbackRepository).existsById(feedbackId)
        verify(feedbackMapper).toFeedback(feedbackDtoRequest, user)
        verify(feedbackRepository).save(feedback)
        verify(feedbackMapper).toDto(feedback)
    }

    @Test
    fun updateByIdShouldThrowExceptionWhenFeedbackDoesNotExist() {
        `when`(feedbackRepository.existsById(feedbackId)).thenReturn(false)

        assertThrows<EntityNotFoundException> {
            feedbackService.updateById(feedbackId, feedbackDtoRequest, user)
        }

        verify(feedbackRepository).existsById(feedbackId)
        verify(feedbackMapper, never()).toFeedback(feedbackDtoRequest, user)
        verify(feedbackRepository, never()).save(feedback)
        verify(feedbackMapper, never()).toDto(feedback)
    }

    @Test
    fun deleteByIdShouldDeleteFeedbackWhenExists() {
        `when`(feedbackRepository.existsById(feedbackId)).thenReturn(true)

        val isDeleted = feedbackService.deleteById(feedbackId)

        assertTrue(isDeleted)
        verify(feedbackRepository).existsById(feedbackId)
        verify(feedbackRepository).deleteById(feedbackId)
    }

    @Test
    fun deleteByIdShouldThrowExceptionWhenFeedbackDoesNotExist() {
        `when`(feedbackRepository.existsById(feedbackId)).thenReturn(false)

        assertThrows<EntityNotFoundException> {
            feedbackService.deleteById(feedbackId)
        }

        verify(feedbackRepository).existsById(feedbackId)
        verify(feedbackRepository, never()).deleteById(feedbackId)
    }
}
