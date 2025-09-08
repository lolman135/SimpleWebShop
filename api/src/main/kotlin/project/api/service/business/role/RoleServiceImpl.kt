package project.api.service.business.role

import org.springframework.stereotype.Service
import project.api.dto.request.business.RoleDtoRequest
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

    override fun save(dto: RoleDtoRequest): Role {
        val role = roleMapper.toRole(dto)
        return roleRepository.save(role)
    }

    override fun findAll(): List<Role> = roleRepository.findAll()

    override fun findById(id: UUID): Role = roleRepository.findById(id).orElseThrow {
        EntityNotFoundException("Role with id=$id not found")
    }

    override fun updateById(id: UUID, dto: RoleDtoRequest): Role {
        if (!roleRepository.existsById(id))
            throw EntityNotFoundException("Role with id=$id not found")

        val role = roleMapper.toRole(dto)
        role.id = id
        return roleRepository.save(role)
    }

    override fun getDefaultRole(): Role =
         roleRepository.findRoleByName("ROLE_USER")
             .orElseThrow { EntityNotFoundException("Default role not found") }
}