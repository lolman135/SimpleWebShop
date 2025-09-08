package project.api.mapper.business.role

import org.springframework.stereotype.Component
import project.api.dto.request.business.RoleDtoRequest
import project.api.dto.response.business.RoleDtoResponse
import project.api.entity.Role

@Component
class RoleMapperImpl : RoleMapper {
    override fun toRole(request: RoleDtoRequest) = Role(name = request.name)
    override fun toDto(role: Role) = RoleDtoResponse(
        id = role.id ?: throw IllegalArgumentException("Id is not provided"),
        name = role.name
    )
}