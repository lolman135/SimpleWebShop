package project.api.controller.authentication

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import project.api.dto.request.authentication.LoginRequest
import project.api.dto.request.authentication.RegisterRequest
import project.api.service.authentication.AuthService

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/login")
    fun login(@RequestBody @Valid request: LoginRequest): ResponseEntity<String>{
        val token = authService.login(request)
        return ResponseEntity.ok(token)
    }

    @PostMapping("/register")
    fun register(@RequestBody @Valid request: RegisterRequest): ResponseEntity<String>{
        val token = authService.register(request)
        return ResponseEntity.ok(token)
    }
}