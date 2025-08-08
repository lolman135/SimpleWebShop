package project.api.mapper.user

import org.springframework.stereotype.Component
import project.api.dto.UserDto
import project.api.entity.User
import project.api.repository.role.RoleRepository

@Component
class UserMapperImpl : UserMapper {

    override fun toUser(userDto: UserDto): User {
        val user = User(
            username = userDto.username,
            email = userDto.email,
            password = userDto.password
        )

        return user
    }
}