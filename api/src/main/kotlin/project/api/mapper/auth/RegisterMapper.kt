package project.api.mapper.auth

import project.api.dto.auth.RegisterRequest
import project.api.entity.User

fun toUser(request: RegisterRequest): User{
    return User(
        username = request.username,
        email = request.email,
        password = request.password
    )
}