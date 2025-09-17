package project.api.service.business.feedback

import jakarta.transaction.Transactional
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service
import project.api.dto.request.business.FeedbackDtoRequest
import project.api.dto.response.business.FeedbackDtoResponse
import project.api.entity.User
import project.api.exception.EntityNotFoundException
import project.api.mapper.business.feedback.FeedbackMapper
import project.api.repository.feedback.FeedbackRepository
import java.util.*

@Service
class FeedbackServiceImpl(
    private val feedbackMapper: FeedbackMapper,
    private val feedbackRepository: FeedbackRepository,
) : FeedbackService {

    @Caching(
        evict = [
            CacheEvict(value = ["feedbackList"], allEntries = true),
            CacheEvict(value = ["feedbackForUser"], allEntries = true),
            CacheEvict(value = ["feedbacks"], key = "#id")
        ]
    )
    override fun deleteById(id: UUID): Boolean {
        if (!feedbackRepository.existsById(id))
            throw EntityNotFoundException("Feedback with id=$id not found")
        feedbackRepository.deleteById(id)
        return true
    }

    @Transactional
    @Caching(
        evict = [
            CacheEvict(value = ["feedbackList"], allEntries = true),
            CacheEvict(value = ["feedbackForUser"], key = "#user.username")
        ]
    )
    override fun save(dto: FeedbackDtoRequest, user: User): FeedbackDtoResponse {
        val feedback = feedbackMapper.toFeedback(dto, user)
        val savedFeedback = feedbackRepository.save(feedback)
        return feedbackMapper.toDto(savedFeedback)
    }

    @Transactional
    @Cacheable(value = ["feedbackList"])
    override fun findAll() = feedbackRepository.findAll().map{feedbackMapper.toDto(it)}

    @Transactional
    @Cacheable(value = ["feedbackForUser"], key = "#user.username")
    override fun findAllForUser(user: User) = user.feedbacks.map { feedbackMapper.toDto(it) }.toList()

    @Transactional
    @Cacheable(value = ["feedbacks"], key = "#id")
    override fun findById(id: UUID): FeedbackDtoResponse {
        val feedback = feedbackRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Feedback with id=$id not found") }
        return feedbackMapper.toDto(feedback)
    }

    @Transactional
    @Caching(
        put = [CachePut(value = ["feedbacks"], key = "#id")],
        evict = [
            CacheEvict(value = ["feedbackList"], allEntries = true),
            CacheEvict(value = ["feedbackForUser"], key = "#user.username")
        ]
    )
    override fun updateById(id: UUID, feedbackDtoRequest: FeedbackDtoRequest, user: User): FeedbackDtoResponse {
        if (!feedbackRepository.existsById(id))
            throw EntityNotFoundException("Feedback with id=$id not found")
        val updateFeedback = feedbackMapper.toFeedback(feedbackDtoRequest, user)
        updateFeedback.id = id
        val updatedFeedback = feedbackRepository.save(updateFeedback)
        return feedbackMapper.toDto(updatedFeedback)
    }
}