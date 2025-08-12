package project.api.mapper.feedback

import org.springframework.stereotype.Component
import project.api.dto.FeedbackDto
import project.api.entity.Feedback
import project.api.entity.User
import project.api.repository.product.ProductRepository
import project.api.repository.user.UserRepository
import java.lang.IllegalArgumentException

@Component
class FeedbackMapperImpl(
    val productRepository: ProductRepository
) : FeedbackMapper {

    override fun mapToFeedback(feedbackDto: FeedbackDto, user: User): Feedback {
        val product = productRepository.findById(feedbackDto.productId).orElseThrow{
            IllegalArgumentException("Wrong data provided")
        }

        val feedback = Feedback(
            review = feedbackDto.review,
            rate = feedbackDto.rate,
            user = user,
            product = product
        )

        return feedback
    }
}