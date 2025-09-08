package project.api.mapper.business.feedback

import project.api.dto.request.business.FeedbackDtoRequest
import project.api.dto.response.business.FeedbackDtoResponse
import project.api.entity.Feedback
import project.api.entity.User

interface FeedbackMapper {
    fun toFeedback(request: FeedbackDtoRequest, user: User): Feedback
    fun toDto(feedback: Feedback): FeedbackDtoResponse
}