package project.api.service.business.role

import project.api.dto.RoleDto
import project.api.entity.Role
import java.util.*

interface RoleService {
    fun save(dto: RoleDto): Role
    fun findAll(): List<Role>
    fun findById(): Role
    fun updateById(id: UUID, dto: RoleDto): Role
    fun deleteById(id: UUID): Boolean
}