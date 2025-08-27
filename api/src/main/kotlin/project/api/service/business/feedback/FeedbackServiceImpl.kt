package project.api.service.business.feedback

import org.springframework.stereotype.Service
import project.api.dto.FeedbackDto
import project.api.entity.Feedback
import project.api.entity.User
import project.api.exception.EntityNotFoundException
import project.api.mapper.feedback.FeedbackMapper
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

    override fun save(dto: FeedbackDto, user: User): Feedback {
        val feedback = feedbackMapper.toFeedback(dto, user)
        return feedbackRepository.save(feedback)
    }

    override fun findAll() = feedbackRepository.findAll()

    override fun findAllForUser(user: User) = user.feedbacks.toList()

    override fun findById(id: UUID) = feedbackRepository.findById(id).orElseThrow {
        EntityNotFoundException("Feedback with id=$id not found")
    }

    override fun updateById(id: UUID, feedbackDto: FeedbackDto, user: User): Feedback {
        if (!feedbackRepository.existsById(id))
            throw EntityNotFoundException("Feedback with id=$id not found")
        val updateFeedback = feedbackMapper.toFeedback(feedbackDto, user)
        updateFeedback.id = id
        return feedbackRepository.save(updateFeedback)
    }
}