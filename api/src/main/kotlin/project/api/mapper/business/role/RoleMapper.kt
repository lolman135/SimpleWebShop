package project.api.mapper.business.role

import project.api.dto.request.business.RoleDtoRequest
import project.api.dto.response.business.RoleDtoResponse
import project.api.entity.Role

interface RoleMapper {
    fun toRole(request: RoleDtoRequest): Role
    fun toDto(role: Role): RoleDtoResponse
}