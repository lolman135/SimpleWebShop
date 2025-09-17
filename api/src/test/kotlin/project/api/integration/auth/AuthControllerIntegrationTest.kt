package project.api.integration.auth

import TestCacheConfig
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import project.api.dto.request.authentication.LoginRequest
import project.api.dto.request.authentication.RegisterRequest
import project.api.entity.Role
import project.api.entity.User
import project.api.repository.role.RoleRepository
import project.api.repository.user.UserRepository

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Import(TestCacheConfig::class)
class AuthControllerIntegrationTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    val userRepository: UserRepository,
    val roleRepository: RoleRepository,
    val passwordEncoder: PasswordEncoder
) {

//    @MockitoBean
//    private lateinit var cacheManager: CacheManager
    private lateinit var existingUser: User

    @BeforeEach
    fun setUp() {
        existingUser = userRepository.save(
            User(
                username = "John123",
                email = "test@test.com",
                password = passwordEncoder.encode("Pass#123")
            )
        )
    }

    @Test
    fun loginReturnsToken() {
        val request = LoginRequest("John123", "Pass#123")

        mockMvc.post("/api/v1/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$") { isNotEmpty() }
        }
    }

    @Test
    fun registerCreatesUser() {
        roleRepository.save(Role(name = "ROLE_USER"))
        val request = RegisterRequest("Jane456", "jane@test.com", "Pass#123")

        mockMvc.post("/api/v1/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$") { isNotEmpty() }
        }
    }

    @Test
    fun loginReturns400OnInvalidUsername() {
        val invalidRequest = LoginRequest(username = "J", password = "Pass#123")

        mockMvc.post("/api/v1/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(invalidRequest)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun registerReturns400OnInvalidEmail() {
        val invalidRequest = RegisterRequest(username = "John123", email = "wrong", password = "Pass#123")

        mockMvc.post("/api/v1/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(invalidRequest)
        }.andExpect {
            status { isBadRequest() }
        }
    }
}
