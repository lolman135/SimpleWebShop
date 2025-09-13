package project.api.service.authentication

import project.api.dto.request.authentication.LoginRequest
import project.api.dto.request.authentication.RegisterRequest

interface AuthService {
    fun login(request: LoginRequest): String
    fun register(request: RegisterRequest): String
}