package project.api.mapper.role

import project.api.dto.RoleDto
import project.api.entity.Role

interface RoleMapper {

    fun toRole(roleDto: RoleDto): Role
}