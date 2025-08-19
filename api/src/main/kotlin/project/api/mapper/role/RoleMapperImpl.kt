package project.api.mapper.role

import org.springframework.stereotype.Component
import project.api.dto.RoleDto
import project.api.entity.Role

@Component
class RoleMapperImpl : RoleMapper {
    override fun toRole(roleDto: RoleDto) = Role(name = roleDto.name)
}