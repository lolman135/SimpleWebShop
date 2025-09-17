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
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import project.api.dto.request.business.RoleDtoRequest
import project.api.entity.Role
import project.api.repository.role.RoleRepository

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestCacheConfig::class)
class RoleControllerIntegrationTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    val roleRepository: RoleRepository
) {
    private lateinit var existingRole: Role

    @BeforeEach
    fun setUp() {
        roleRepository.deleteAll()
        existingRole = roleRepository.save(Role(name = "ROLE_USER"))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun addRoleCreatesRole() {
        val request = RoleDtoRequest(name = "ROLE_MANAGER")

        mockMvc.post("/api/v1/roles") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.name") { value("ROLE_MANAGER") }
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun getRoleByIdReturnsRole() {
        mockMvc.get("/api/v1/roles/${existingRole.id}")
            .andExpect {
                status { isOk() }
                jsonPath("$.name") { value(existingRole.name) }
            }
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun getAllRolesReturnsRoles() {
        mockMvc.get("/api/v1/roles")
            .andExpect {
                status { isOk() }
                jsonPath("$[0].name") { value(existingRole.name) }
            }
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun updateRoleByIdUpdatesRole() {
        val request = RoleDtoRequest(name = "ROLE_MANAGER")

        mockMvc.put("/api/v1/roles/${existingRole.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.name") { value("ROLE_MANAGER") }
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun deleteRoleByIdDeletesRole() {
        mockMvc.delete("/api/v1/roles/${existingRole.id}")
            .andExpect {
                status { isNoContent() }
            }
    }
}
