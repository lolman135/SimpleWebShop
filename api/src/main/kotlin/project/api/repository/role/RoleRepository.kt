package project.api.repository.role

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import project.api.entity.Role
import java.util.*

@Repository
interface RoleRepository : JpaRepository<Role, UUID> {
    fun findRoleByName(name: String): Optional<Role>
}