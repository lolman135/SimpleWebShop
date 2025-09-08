package project.api.mapper.business.feedback

import org.springframework.stereotype.Component
import project.api.dto.request.business.FeedbackDtoRequest
import project.api.dto.response.business.FeedbackDtoResponse
import project.api.entity.Feedback
import project.api.entity.User
import project.api.mapper.business.subDto.toSubDto
import project.api.repository.product.ProductRepository
import kotlin.IllegalArgumentException

@Component
class FeedbackMapperImpl(
    val productRepository: ProductRepository
) : FeedbackMapper {

    override fun toFeedback(request: FeedbackDtoRequest, user: User): Feedback {
        val product = productRepository.findById(request.productId).orElseThrow{
            IllegalArgumentException("Wrong data provided")
        }

        val feedback = Feedback(
            review = request.review,
            rate = request.rate,
            user = user,
            product = product
        )

        return feedback
    }

    override fun toDto(feedback: Feedback) = FeedbackDtoResponse(
        id = feedback.id ?: throw IllegalArgumentException("Id is not provided"),
        review = feedback.review ?: "",
        rate = feedback.rate,
        user = feedback.user.toSubDto(),
        product = feedback.product.toSubDto()
    )
}