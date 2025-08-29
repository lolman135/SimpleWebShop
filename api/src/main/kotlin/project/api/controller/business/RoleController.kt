package project.api.controller.business

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import project.api.dto.auth.RegisterRequest
import project.api.dto.business.RoleDto
import project.api.entity.Role
import project.api.service.business.role.RoleService

@RestController
@RequestMapping("/api/v1")
class RoleController(
    private val roleService: RoleService,
) {

    @PostMapping("/roles")
    fun addRole(@RequestBody request: RoleDto): ResponseEntity<Role>{
        val role = roleService.save(request)
        return ResponseEntity.ok(role)
    }
}