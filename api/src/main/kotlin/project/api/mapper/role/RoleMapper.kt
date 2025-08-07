package project.api.mapper.role

import project.api.dto.RoleDto
import project.api.entity.Role

interface RoleMapper {

    fun mapToRole(roleDto: RoleDto): Role
}