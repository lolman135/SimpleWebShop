package project.api.controller.business

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import project.api.dto.request.business.RoleDtoRequest
import project.api.dto.response.business.RoleDtoResponse
import project.api.service.business.role.RoleService
import java.net.URI
import java.util.UUID

@RestController
@RequestMapping("/api/v1/roles")
class RoleController(private val roleService: RoleService) {

    @PostMapping
    @Operation(summary = "Creating new role", description = "Creates new role if not exists yet")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Created successfully. Returns created role"),
            ApiResponse(responseCode = "400", description = "Validation error"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden"),
            ApiResponse(responseCode = "409", description = "Conflict")
        ]
    )
    fun addRole(@RequestBody @Valid request: RoleDtoRequest): ResponseEntity<RoleDtoResponse> {
        val response = roleService.save(request)
        val location = URI.create("/roles/${response.id}")
        return ResponseEntity.created(location).body(response)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Returns role", description = "Returns role by id if exists")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returns category"),
            ApiResponse(responseCode = "404", description = "Not found"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden")
        ]
    )
    fun getRoleById(@PathVariable id: UUID) = ResponseEntity.ok(roleService.findById(id))

    @GetMapping
    @Operation(
        summary = "Returns all roles",
        description = "Returns all roles as list. If nothing to return, than returns empty list")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returns list of roles"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden")
        ]
    )
    fun getAllRoles() = ResponseEntity.ok(roleService.findAll())

    @PutMapping("/{id}")
    @Operation(summary = "Updating existed role", description = "Updates role and returns it in response")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Updated successfully. Returns updated role"),
            ApiResponse(responseCode = "400", description = "Validation error"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden"),
            ApiResponse(responseCode = "404", description = "Not found")
        ]
    )
    fun updateRoleById(@PathVariable id: UUID, @RequestBody @Valid request: RoleDtoRequest) =
        ResponseEntity.ok(roleService.updateById(id, request))

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleting existed role", description = "Deletes role. Returns nothing")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Deleted successfully. No contend"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden"),
            ApiResponse(responseCode = "404", description = "Not found")
        ]
    )
    fun deleteRoleById(@PathVariable id: UUID): ResponseEntity<Void> {
        roleService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}