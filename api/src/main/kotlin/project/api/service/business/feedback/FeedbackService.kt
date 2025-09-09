package project.api.service.business.feedback

import project.api.dto.request.business.FeedbackDtoRequest
import project.api.dto.response.business.FeedbackDtoResponse
import project.api.entity.User
import java.util.UUID

interface FeedbackService {
    fun save(dto: FeedbackDtoRequest, user: User): FeedbackDtoResponse
    fun findAll(): List<FeedbackDtoResponse>
    fun findAllForUser(user: User): List<FeedbackDtoResponse>
    fun findById(id: UUID): FeedbackDtoResponse
    fun updateById(id: UUID, feedbackDtoRequest: FeedbackDtoRequest, user: User): FeedbackDtoResponse
    fun deleteById(id: UUID): Boolean
}