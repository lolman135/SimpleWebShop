package project.api.mapper.business.role

import org.springframework.stereotype.Component
import project.api.dto.business.RoleDto
import project.api.entity.Role

@Component
class RoleMapperImpl : RoleMapper {
    override fun toRole(roleDto: RoleDto) = Role(name = roleDto.name)
}