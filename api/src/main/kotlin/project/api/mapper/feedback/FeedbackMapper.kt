package project.api.mapper.feedback

import project.api.dto.FeedbackDto
import project.api.entity.Feedback
import project.api.entity.User

interface FeedbackMapper {
    fun mapToFeedback(feedbackDto: FeedbackDto, user: User): Feedback
}