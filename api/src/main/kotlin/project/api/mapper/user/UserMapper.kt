package project.api.mapper.user

import project.api.dto.UserDto
import project.api.entity.User

interface UserMapper {

    fun toUser(userDto: UserDto): User
}