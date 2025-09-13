package project.api.unit.controller.authentication

import project.api.controller.authentication.AuthController
import project.api.dto.request.authentication.LoginRequest
import project.api.dto.request.authentication.RegisterRequest
import project.api.service.authentication.AuthService

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.*
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import project.api.config.TestSecurityConfig
import project.api.security.JwtAuthFilter

@WebMvcTest(controllers = [AuthController::class],
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [JwtAuthFilter::class]
        )
    ]
)
@Import(TestSecurityConfig::class)
class AuthControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {

    @MockitoBean
    lateinit var authService: AuthService


    private lateinit var validLoginRequest: LoginRequest
    private lateinit var validRegisterRequest: RegisterRequest

    @BeforeEach
    fun setUp() {
        validLoginRequest = LoginRequest(username = "John123", password = "Pass#123")
        validRegisterRequest = RegisterRequest(username = "John123", email = "test@test.com", password = "Pass#123")
    }

    @Test
    fun loginReturnsToken() {
        Mockito.`when`(authService.login(validLoginRequest)).thenReturn("jwt-token")

        mockMvc.post("/api/v1/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(validLoginRequest)
        }
            .andExpect {
                status { isOk() }
                content { string("jwt-token") }
            }

        Mockito.verify(authService).login(validLoginRequest)
    }

    @Test
    fun registerReturnsToken() {
        Mockito.`when`(authService.register(validRegisterRequest)).thenReturn("jwt-token")

        mockMvc.post("/api/v1/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(validRegisterRequest)
        }
            .andExpect {
                status { isOk() }
                content { string("jwt-token") }
            }

        Mockito.verify(authService).register(validRegisterRequest)
    }

    @Test
    fun loginReturns400OnInvalidUsername() {
        val invalidRequest = LoginRequest(username = "J", password = "Pass#123")

        mockMvc.post("/api/v1/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(invalidRequest)
        }
            .andExpect {
                status { isBadRequest() }
            }
    }

    @Test
    fun registerReturns400OnInvalidEmail() {
        val invalidRequest = RegisterRequest(username = "John123", email = "wrong", password = "Pass#123")

        mockMvc.post("/api/v1/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(invalidRequest)
        }
            .andExpect {
                status { isBadRequest() }
            }
    }
}
