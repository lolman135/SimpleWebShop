package project.api.mapper.auth

import project.api.dto.request.auth.RegisterRequest
import project.api.entity.User

fun RegisterRequest.toUser(): User = this.toUserInternal()

private fun RegisterRequest.toUserInternal() = User(
    username = this.username,
    email = this.email,
    password = this.password
)
