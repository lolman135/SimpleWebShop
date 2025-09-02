package project.api.service.authentication

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import project.api.dto.auth.LoginRequest
import project.api.dto.auth.RegisterRequest
import project.api.security.JwtTokenProvider
import project.api.service.business.user.UserService

@Service
class AuthServiceImpl(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) : AuthService {

    override fun login(request: LoginRequest): String {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        return jwtTokenProvider.generateToken(request.username)
    }

    override fun register(request: RegisterRequest): String {
        val encryptedRequest = request.copy(password = passwordEncoder.encode(request.password))
        val registeredUser = userService.save(encryptedRequest)
        return jwtTokenProvider.generateToken(registeredUser.username)
    }
}