package project.api.mapper.business.role

import project.api.dto.business.RoleDto
import project.api.entity.Role

interface RoleMapper {

    fun toRole(roleDto: RoleDto): Role
}