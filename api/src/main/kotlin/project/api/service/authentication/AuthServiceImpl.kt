package project.api.service.authentication

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.stereotype.Service
import project.api.dto.auth.LoginRequest
import project.api.dto.auth.RegisterRequest
import project.api.repository.user.UserRepository

@Service
class AuthServiceImpl(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
) : AuthService {

    override fun login(request: LoginRequest): String {
        TODO("Not yet implemented")
    }

    override fun register(request: RegisterRequest): String {
        TODO("Not yet implemented")
    }
}