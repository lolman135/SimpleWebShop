package project.api.service.business.role

import project.api.dto.request.business.RoleDtoRequest
import project.api.dto.response.business.RoleDtoResponse
import project.api.entity.Role
import java.util.*

interface RoleService {
    fun save(dto: RoleDtoRequest): RoleDtoResponse
    fun findAll(): List<RoleDtoResponse>
    fun findById(id: UUID): RoleDtoResponse
    fun updateById(id: UUID, dto: RoleDtoRequest): RoleDtoResponse
    fun deleteById(id: UUID): Boolean
    fun getDefaultRole(): Role
}