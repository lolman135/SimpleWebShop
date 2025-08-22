package project.api.mapper.feedback

import org.springframework.stereotype.Component
import project.api.dto.FeedbackDto
import project.api.entity.Feedback
import project.api.entity.User

//@Component
class FeedbackMapperImpl2 : FeedbackMapper {
    override fun toFeedback(feedbackDto: FeedbackDto, user: User): Feedback {
        TODO("Not yet implemented")
    }
}