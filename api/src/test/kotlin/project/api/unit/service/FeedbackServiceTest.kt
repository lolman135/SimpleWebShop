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
import project.api.dto.business.FeedbackDto
import project.api.entity.*
import project.api.exception.EntityNotFoundException
import project.api.mapper.business.feedback.FeedbackMapper
import project.api.repository.feedback.FeedbackRepository
import project.api.service.business.feedback.FeedbackServiceImpl
import java.util.*
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class FeedbackServiceTest {

    @Mock
    private lateinit var feedbackMapper: FeedbackMapper
    @Mock
    private lateinit var feedbackRepository: FeedbackRepository

    @InjectMocks
    private lateinit var feedbackService: FeedbackServiceImpl

    private lateinit var feedbackId: UUID
    private lateinit var userId: UUID
    private lateinit var productId: UUID
    private lateinit var categoryId: UUID
    private lateinit var feedbackDto: FeedbackDto
    private lateinit var user: User
    private lateinit var category: Category
    private lateinit var product: Product
    private lateinit var feedback: Feedback

    @BeforeEach
    fun setUp(){
        feedbackId = UUID.randomUUID()
        productId = UUID.randomUUID()
        userId = UUID.randomUUID()
        categoryId = UUID.randomUUID()

        category = Category(
            id = categoryId,
            name = "Test category",
        )

        feedbackDto = FeedbackDto(
            productId = productId,
            rate = 5,
            review = "Test review"
        )

        user = User(
            id = userId,
            username = "JohnDoe",
            email = "john@example.com",
            password = "securePass123",
        )

        product = Product(
            id = productId,
            name = "Test name",
            description = "Test description",
            price = 10,
            imageUrl = "https://imgBase:/testImg.com",
            category = category // добавляем обязательную категорию
        )

        feedback = Feedback(
            id = feedbackId,
            user = user,
            product = product,
            rate = 5,
            review = "Test review"
        )
    }

    @Test
    fun saveShouldMapDtoToFeedbackAndSaveEntity(){
        `when`(feedbackMapper.toFeedback(feedbackDto, user)).thenReturn(feedback)
        `when`(feedbackRepository.save(feedback)).thenReturn(feedback)

        val result = feedbackService.save(feedbackDto, user)

        assertEquals(feedback, result)
        verify(feedbackRepository).save(feedback)
        verify(feedbackMapper).toFeedback(feedbackDto, user)
    }

    @Test
    fun findAllShouldReturnListOfFeedbacks() {
        val feedbacks = listOf(feedback)
        `when`(feedbackRepository.findAll()).thenReturn(feedbacks)

        val result = feedbackService.findAll()

        assertEquals(feedbacks, result)
        verify(feedbackRepository).findAll()
    }

    @Test
    fun findByIdShouldReturnFeedbackIfExists() {
        `when`(feedbackRepository.findById(feedbackId)).thenReturn(Optional.of(feedback))

        val result = feedbackService.findById(feedbackId)

        assertEquals(feedback, result)
        verify(feedbackRepository).findById(feedbackId)
    }

    @Test
    fun findByIdShouldThrowExceptionWhenFeedbackDoesNotExist() {
        `when`(feedbackRepository.findById(feedbackId)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            feedbackService.findById(feedbackId)
        }

        verify(feedbackRepository).findById(feedbackId)
    }

    @Test
    fun updateByIdShouldUpdateFeedbackWhenExists() {
        `when`(feedbackRepository.existsById(feedbackId)).thenReturn(true)
        `when`(feedbackMapper.toFeedback(feedbackDto, user)).thenReturn(feedback)
        `when`(feedbackRepository.save(feedback)).thenReturn(feedback)

        val result = feedbackService.updateById(feedbackId, feedbackDto, user)

        assertEquals(feedback, result)
        verify(feedbackRepository).existsById(feedbackId)
        verify(feedbackMapper).toFeedback(feedbackDto, user)
        verify(feedbackRepository).save(feedback)
    }

    @Test
    fun updateByIdShouldThrowExceptionWhenFeedbackDoesNotExist() {
        `when`(feedbackRepository.existsById(feedbackId)).thenReturn(false)

        assertThrows<EntityNotFoundException> {
            feedbackService.updateById(feedbackId, feedbackDto, user)
        }

        verify(feedbackRepository).existsById(feedbackId)
        verify(feedbackMapper, never()).toFeedback(feedbackDto, user)
        verify(feedbackRepository, never()).save(feedback)
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
