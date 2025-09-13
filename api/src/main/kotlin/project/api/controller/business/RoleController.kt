package project.api.controller.business

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
import project.api.service.business.role.RoleService
import java.util.UUID

@RestController
@RequestMapping("/api/v1/roles")
class RoleController(private val roleService: RoleService) {

    @PostMapping
    fun addRole(@RequestBody @Valid request: RoleDtoRequest) = ResponseEntity.ok(roleService.save(request))

    @GetMapping("/{id}")
    fun getRoleById(@PathVariable id: UUID) = ResponseEntity.ok(roleService.findById(id))

    @GetMapping
    fun getAllRoles() = ResponseEntity.ok(roleService.findAll())

    @PutMapping("/{id}")
    fun updateRoleById(@PathVariable id: UUID, @RequestBody @Valid request: RoleDtoRequest) =
        ResponseEntity.ok(roleService.updateById(id, request))

    @DeleteMapping("/{id}")
    fun deleteRoleById(@PathVariable id: UUID): ResponseEntity<Void> {
        roleService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}