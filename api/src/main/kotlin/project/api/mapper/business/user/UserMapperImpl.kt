package project.api.mapper.business.user

import org.springframework.stereotype.Component
import project.api.dto.response.business.UserDtoResponse
import project.api.entity.User
import project.api.mapper.business.subDto.toSubDto
import project.api.repository.role.RoleRepository
import project.api.repository.order.OrderRepository
import project.api.repository.feedback.FeedbackRepository

@Component
class UserMapperImpl(
    val roleRepository: RoleRepository,
    val orderRepository: OrderRepository,
    val feedbackRepository: FeedbackRepository
) : UserMapper {

    override fun toDto(user: User) = UserDtoResponse(
        id = user.id ?: throw IllegalArgumentException("Id is not provided"),
        username = user.username,
        email = user.email,
        roles = user.roles.map { it.name },
        feedbacks = user.feedbacks.map { it.toSubDto() },
        orders = user.orders.map { it.toSubDto() }
    )
}