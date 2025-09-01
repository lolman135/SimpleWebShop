package project.api.controller.business

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import project.api.dto.business.RoleDto
import project.api.entity.Role
import project.api.service.business.role.RoleService
import java.util.UUID

@RestController
@RequestMapping("/api/v1/roles")
class RoleController(private val roleService: RoleService) {

    @PostMapping
    fun addRole(@RequestBody @Valid request: RoleDto): ResponseEntity<Role>{
        val role = roleService.save(request)
        return ResponseEntity.ok(role)
    }

    @GetMapping("/{id}")
    fun getRole(@PathVariable id: UUID): ResponseEntity<Role>{
        val role = roleService.findById(id)
        return ResponseEntity.ok(role)
    }

    @GetMapping
    fun getAllRoles(): ResponseEntity<List<Role>> = ResponseEntity.ok(roleService.findAll())
}