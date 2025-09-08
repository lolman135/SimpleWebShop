package project.api.mapper.business.user

import project.api.dto.request.business.UserDtoRequest
import project.api.dto.response.business.UserDtoResponse
import project.api.entity.User

interface UserMapper {
    fun toUser(request: UserDtoRequest): User
    fun toDto(user: User): UserDtoResponse
}