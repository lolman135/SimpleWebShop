package project.api.mapper.business.user

import org.springframework.stereotype.Component
import project.api.dto.business.UserDto
import project.api.entity.User
import project.api.repository.role.RoleRepository
import project.api.repository.order.OrderRepository
import project.api.repository.feedback.FeedbackRepository

@Component
class UserMapperImpl(
    val roleRepository: RoleRepository,
    val orderRepository: OrderRepository,
    val feedbackRepository: FeedbackRepository
) : UserMapper {

    override fun toUser(userDto: UserDto): User {
        val user = User(
            username = userDto.username,
            email = userDto.email,
            password = userDto.password
        )

        user.roles = userDto.roleIds.map {
            roleId -> roleRepository.findById(roleId).orElseThrow {
                IllegalArgumentException("Wrong Id!")
            }
        }.toMutableSet()

        user.orders = userDto.orderIds.map {
            orderId -> orderRepository.findById(orderId).orElseThrow {
                IllegalArgumentException("Wrong Id!")
            }
        }.toMutableSet()

        user.feedbacks = userDto.feedbackIds.map {
                feedbackId -> feedbackRepository.findById(feedbackId).orElseThrow {
            IllegalArgumentException("Wrong Id!")
        }
        }.toMutableSet()

        return user
    }
}