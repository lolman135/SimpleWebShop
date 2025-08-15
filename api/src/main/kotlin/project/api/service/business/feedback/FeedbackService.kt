package project.api.service.business.feedback

import project.api.dto.FeedbackDto
import project.api.entity.Feedback
import project.api.entity.User
import java.util.UUID

interface FeedbackService {
    fun save(dto: FeedbackDto, user: User)
    fun findAll(): List<Feedback>
    fun findAllForUser(user: User): List<Feedback>
    fun findById(id: UUID): Feedback
    fun updateById(id: UUID, feedbackDto: FeedbackDto): Feedback
    fun deleteById(id: UUID): Boolean
}