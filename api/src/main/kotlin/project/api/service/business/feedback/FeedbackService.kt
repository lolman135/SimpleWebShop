package project.api.service.business.feedback

import project.api.dto.request.business.FeedbackDtoRequest
import project.api.entity.Feedback
import project.api.entity.User
import java.util.UUID

interface FeedbackService {
    fun save(dto: FeedbackDtoRequest, user: User): Feedback
    fun findAll(): List<Feedback>
    fun findAllForUser(user: User): List<Feedback>
    fun findById(id: UUID): Feedback
    fun updateById(id: UUID, feedbackDtoRequest: FeedbackDtoRequest, user: User): Feedback
    fun deleteById(id: UUID): Boolean
}