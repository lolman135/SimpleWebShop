package project.api.controller.authentication

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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
    @Operation(summary = "Login process", description = "Authorize user and return access token (jwt)")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Success authentication, returns JWT"),
            ApiResponse(responseCode = "400", description = "Validation error"),
            ApiResponse(responseCode = "401", description = "Invalid username or password")
        ]
    )
    fun login(@RequestBody @Valid request: LoginRequest): ResponseEntity<String>{
        val token = authService.login(request)
        return ResponseEntity.ok(token)
    }

    @Operation(summary = "Register process", description = "Add user to database and return access token (jwt)")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Success registration, returns JWT"),
            ApiResponse(responseCode = "400", description = "Validation error"),
        ]
    )
    @PostMapping("/register")
    fun register(@RequestBody @Valid request: RegisterRequest): ResponseEntity<String>{
        val token = authService.register(request)
        return ResponseEntity.ok(token)
    }
}