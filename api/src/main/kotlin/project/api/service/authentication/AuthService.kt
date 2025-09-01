package project.api.service.authentication

import project.api.dto.auth.LoginRequest
import project.api.dto.auth.RegisterRequest

interface AuthService {
    fun login(request: LoginRequest): String
    fun register(request: RegisterRequest): String
}