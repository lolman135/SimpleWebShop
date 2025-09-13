package project.api.service.authentication

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import project.api.dto.request.authentication.LoginRequest
import project.api.dto.request.authentication.RegisterRequest
import project.api.exception.InvalidCredentialsException
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
        try {
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.username, request.password)
            )
            SecurityContextHolder.getContext().authentication = authentication

            val authorizedUser = userService.findByUsername(request.username)
            return jwtTokenProvider.generateToken(authorizedUser.id)
        } catch (ex: Exception){
            throw InvalidCredentialsException("Invalid username or password")
        }
    }

    override fun register(request: RegisterRequest): String {
        val encryptedRequest = request.copy(password = passwordEncoder.encode(request.password))
        val registeredUser = userService.save(encryptedRequest)
        return jwtTokenProvider.generateToken(registeredUser.id!!)
    }
}