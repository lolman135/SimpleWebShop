package project.api.unit.controller.business

import org.springframework.test.context.bean.override.mockito.MockitoBean
import project.api.controller.business.RoleController
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import project.api.dto.request.business.RoleDtoRequest
import project.api.dto.response.business.RoleDtoResponse
import project.api.service.business.role.RoleService
import java.util.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(RoleController::class)
@AutoConfigureMockMvc(addFilters = false)
class RoleControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper
) {

    @MockitoBean
    private lateinit var roleService: RoleService
    @MockitoBean
    private lateinit var jwtAuthFilter: project.api.security.JwtAuthFilter
    @MockitoBean
    private lateinit var jwtTokenProvider: project.api.security.JwtTokenProvider

    private lateinit var roleId: UUID
    private lateinit var request: RoleDtoRequest
    private lateinit var response: RoleDtoResponse
    private lateinit var updatedRoleRequest: RoleDtoRequest
    private lateinit var updatedRoleResponse: RoleDtoResponse

    @BeforeEach
    fun setUp() {
        roleId = UUID.randomUUID()
        request = RoleDtoRequest("ADMIN")
        response = RoleDtoResponse(roleId, "ADMIN")
        updatedRoleRequest = RoleDtoRequest("SUPER_ADMIN")
        updatedRoleResponse = RoleDtoResponse(roleId, "SUPER_ADMIN")
    }

    @Test
    fun addRoleReturns201AndSavedRole() {
        given(roleService.save(request)).willReturn(response)

        mockMvc.perform(
            post("/api/v1/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "/roles/$roleId"))
            .andExpect(jsonPath("$.id").value(roleId.toString()))
            .andExpect(jsonPath("$.name").value("ADMIN"))

        verify(roleService).save(request)
    }

    @Test
    fun getRoleByIdReturnsRole() {
        given(roleService.findById(roleId)).willReturn(response)

        mockMvc.perform(get("/api/v1/roles/{id}", roleId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(roleId.toString()))
            .andExpect(jsonPath("$.name").value("ADMIN"))

        verify(roleService).findById(roleId)
    }

    @Test
    fun getAllRolesReturnsList() {
        given(roleService.findAll()).willReturn(listOf(response))

        mockMvc.perform(get("/api/v1/roles"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(roleId.toString()))
            .andExpect(jsonPath("$[0].name").value("ADMIN"))

        verify(roleService).findAll()
    }

    @Test
    fun updateRoleByIdReturnsUpdatedRole() {
        given(roleService.updateById(roleId, updatedRoleRequest)).willReturn(updatedRoleResponse)

        mockMvc.perform(
            put("/api/v1/roles/{id}", roleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRoleRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(roleId.toString()))
            .andExpect(jsonPath("$.name").value("SUPER_ADMIN"))

        verify(roleService).updateById(roleId, updatedRoleRequest)
    }

    @Test
    fun deleteRoleByIdReturnsOk() {
        mockMvc.perform(delete("/api/v1/roles/{id}", roleId))
            .andExpect(status().isNoContent)

        verify(roleService).deleteById(roleId)
    }
}
