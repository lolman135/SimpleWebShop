package project.api.integration.business

import TestCacheConfig
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.*
import project.api.config.TestSecurityConfig
import project.api.dto.request.business.UserDtoUpdateMeRequest
import project.api.dto.request.business.UserDtoUpdateRequest
import project.api.entity.User
import project.api.repository.user.UserRepository
import project.api.security.CustomUserDetails
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig::class, TestCacheConfig::class)
class UserControllerIntegrationTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    val userRepository: UserRepository
) {

    private lateinit var existingUser: User

    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()

        existingUser = userRepository.save(
            User(
                username = "testuser",
                email = "testuser@example.com",
                password = "password"
            )
        )

        val userDetails = CustomUserDetails(existingUser)
        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        SecurityContextHolder.getContext().authentication = authentication
    }

    @Test
    @WithMockUser(username = "testuser", roles = ["ADMIN"])
    fun getUserByIdReturnsUser() {
        mockMvc.get("/api/v1/users/${existingUser.id}")
            .andExpect {
                status { isOk() }
                jsonPath("$.username") { value("testuser") }
                jsonPath("$.email") { value("testuser@example.com") }
            }
    }

    @Test
    @WithMockUser(username = "testuser", roles = ["USER"])
    fun getMeReturnsCurrentUser() {
        mockMvc.get("/api/v1/users/me")
            .andExpect {
                status { isOk() }
                jsonPath("$.username") { value("testuser") }
                jsonPath("$.email") { value("testuser@example.com") }
            }
    }

    @Test
    @WithMockUser(username = "testuser", roles = ["ADMIN"])
    fun updateUserByIdUpdatesUser() {
        val request = UserDtoUpdateRequest(
            username = "updatedUser",
            email = "updated@example.com"
        )

        mockMvc.patch("/api/v1/users/${existingUser.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.username") { value("updatedUser") }
            jsonPath("$.email") { value("updated@example.com") }
        }
    }

    @Test
    @WithMockUser(username = "testuser", roles = ["USER"])
    fun updateMeUpdatesCurrentUser() {
        val request = UserDtoUpdateMeRequest(
            username = "meUpdated",
            email = "meupdated@example.com"
        )

        mockMvc.patch("/api/v1/users/me") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.username") { value("meUpdated") }
            jsonPath("$.email") { value("meupdated@example.com") }
        }
    }

    @Test
    @WithMockUser(username = "testuser", roles = ["ADMIN"])
    fun deleteUserByIdDeletesUser() {
        mockMvc.delete("/api/v1/users/${existingUser.id}")
            .andExpect {
                status { isNoContent() }
            }
    }

    @Test
    @WithMockUser(username = "testuser", roles = ["USER"])
    fun deleteMeDeletesCurrentUser() {
        mockMvc.delete("/api/v1/users/me")
            .andExpect {
                status { isNoContent() }
            }
    }
    @Test
    @WithMockUser(username = "testuser", roles = ["USER"])
    fun getUsersByUsernamePrefixReturnsMatchingUsers() {
        val anotherUser = userRepository.save(
            User(username = "anotherUser", email = "another@example.com", password = "pass")
        )

        mockMvc.get("/api/v1/users/username") {
            param("prefix", "test")
        }.andExpect {
            status { isOk() }
            jsonPath("$[0].username") { value("testuser") }
        }

        mockMvc.get("/api/v1/users/username") {
            param("prefix", "another")
        }.andExpect {
            status { isOk() }
            jsonPath("$[0].username") { value("anotherUser") }
        }
    }

    @Test
    @WithMockUser(username = "testuser", roles = ["ADMIN"])
    fun getAllUsersReturnsAllUsers() {
        val anotherUser = userRepository.save(
            User(username = "anotherUser", email = "another@example.com", password = "pass")
        )

        mockMvc.get("/api/v1/users")
            .andExpect {
                status { isOk() }
                jsonPath("$[0].username") { value("testuser") }
                jsonPath("$[1].username") { value("anotherUser") }
            }
    }
}
