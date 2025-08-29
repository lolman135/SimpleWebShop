package project.api.mapper.business.user

import project.api.dto.business.UserDto
import project.api.entity.User

interface UserMapper {

    fun toUser(userDto: UserDto): User
}