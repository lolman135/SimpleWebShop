package project.api.controller.auth

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import project.api.dto.auth.LoginRequest
import project.api.dto.auth.RegisterRequest
import project.api.service.authentication.AuthService

@Controller
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<String>{
        val token = authService.login(request)
        return ResponseEntity.ok(token)
    }

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<String>{
        val token = authService.register(request)
        return ResponseEntity.ok(token)
    }

}