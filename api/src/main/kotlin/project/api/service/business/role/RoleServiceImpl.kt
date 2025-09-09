package project.api.service.business.role

import org.springframework.stereotype.Service
import project.api.dto.request.business.RoleDtoRequest
import project.api.dto.response.business.RoleDtoResponse
import project.api.entity.Role
import project.api.exception.EntityNotFoundException
import project.api.mapper.business.role.RoleMapper
import project.api.repository.role.RoleRepository
import java.util.*

@Service
class RoleServiceImpl(
    private val roleRepository: RoleRepository,
    private val roleMapper: RoleMapper,
) : RoleService {

    override fun deleteById(id: UUID): Boolean {
        if (!roleRepository.existsById(id))
            throw EntityNotFoundException("Role with id=$id not found")

        roleRepository.deleteById(id)
        return true
    }

    override fun save(dto: RoleDtoRequest): RoleDtoResponse {
        val role = roleMapper.toRole(dto)
        val savedRole = roleRepository.save(role)
        return roleMapper.toDto(savedRole)
    }

    override fun findAll(): List<RoleDtoResponse> = roleRepository.findAll().map { roleMapper.toDto(it) }

    override fun findById(id: UUID): RoleDtoResponse {
        val role = roleRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Role with id=$id not found") }
        return roleMapper.toDto(role)
    }

    override fun updateById(id: UUID, dto: RoleDtoRequest): RoleDtoResponse {
        if (!roleRepository.existsById(id))
            throw EntityNotFoundException("Role with id=$id not found")

        val role = roleMapper.toRole(dto)
        role.id = id
        val updatedRole = roleRepository.save(role)
        return roleMapper.toDto(updatedRole)
    }

    override fun getDefaultRole(): Role =
        roleRepository.findRoleByName("ROLE_USER")
            .orElseThrow { EntityNotFoundException("Default role not found") }
}