package project.api.mapper.business.feedback

import project.api.dto.business.FeedbackDto
import project.api.entity.Feedback
import project.api.entity.User

//@Component
class FeedbackMapperImpl2 : FeedbackMapper {
    override fun toFeedback(feedbackDto: FeedbackDto, user: User): Feedback {
        TODO("Not yet implemented")
    }
}