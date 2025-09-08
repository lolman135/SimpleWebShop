package project.api.service.authentication

import project.api.dto.request.auth.LoginRequest
import project.api.dto.request.auth.RegisterRequest

interface AuthService {
    fun login(request: LoginRequest): String
    fun register(request: RegisterRequest): String
}