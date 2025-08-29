package project.api.mapper.business.feedback

import project.api.dto.business.FeedbackDto
import project.api.entity.Feedback
import project.api.entity.User

interface FeedbackMapper {
    fun toFeedback(feedbackDto: FeedbackDto, user: User): Feedback
}