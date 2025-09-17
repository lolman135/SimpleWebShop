package project.api.service.business.role

import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
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

    @Caching(
        evict = [
            CacheEvict(value = ["roleList"], allEntries = true),
            CacheEvict(value = ["roles"], key = "#id")
        ]
    )
    override fun deleteById(id: UUID): Boolean {
        if (!roleRepository.existsById(id))
            throw EntityNotFoundException("Role with id=$id not found")
        roleRepository.deleteById(id)
        return true
    }

    @CacheEvict(value = ["roleList"], allEntries = true)
    override fun save(dto: RoleDtoRequest): RoleDtoResponse {
        val role = roleMapper.toRole(dto)
        val savedRole = roleRepository.save(role)
        return roleMapper.toDto(savedRole)
    }

    @Cacheable(value = ["roleList"])
    override fun findAll(): List<RoleDtoResponse> = roleRepository.findAll().map { roleMapper.toDto(it) }

    @Cacheable(value = ["roles"], key = "#id")
    override fun findById(id: UUID): RoleDtoResponse {
        val role = roleRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Role with id=$id not found") }
        return roleMapper.toDto(role)
    }

    @Caching(
        put = [CachePut(value = ["roles"], key = "#id")],
        evict = [CacheEvict(value = ["roleList"], allEntries = true)]
    )
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