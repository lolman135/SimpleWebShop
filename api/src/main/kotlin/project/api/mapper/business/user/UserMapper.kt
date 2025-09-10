package project.api.mapper.business.user

import project.api.dto.response.business.UserDtoResponse
import project.api.entity.User

interface UserMapper {
    fun toDto(user: User): UserDtoResponse
}