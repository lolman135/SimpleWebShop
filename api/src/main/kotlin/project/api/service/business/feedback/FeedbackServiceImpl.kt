package project.api.service.business.feedback

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import project.api.dto.request.business.FeedbackDtoRequest
import project.api.entity.Feedback
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

    override fun deleteById(id: UUID): Boolean {
        if (!feedbackRepository.existsById(id))
            throw EntityNotFoundException("Feedback with id=$id not found")
        feedbackRepository.deleteById(id)
        return true
    }

    @Transactional
    override fun save(dto: FeedbackDtoRequest, user: User): Feedback {
        val feedback = feedbackMapper.toFeedback(dto, user)
        return feedbackRepository.save(feedback)
    }

    @Transactional
    override fun findAll() = feedbackRepository.findAll()

    @Transactional
    override fun findAllForUser(user: User) = user.feedbacks.toList()

    @Transactional
    override fun findById(id: UUID) = feedbackRepository.findById(id).orElseThrow {
        EntityNotFoundException("Feedback with id=$id not found")
    }

    @Transactional
    override fun updateById(id: UUID, feedbackDtoRequest: FeedbackDtoRequest, user: User): Feedback {
        if (!feedbackRepository.existsById(id))
            throw EntityNotFoundException("Feedback with id=$id not found")
        val updateFeedback = feedbackMapper.toFeedback(feedbackDtoRequest, user)
        updateFeedback.id = id
        return feedbackRepository.save(updateFeedback)
    }
}