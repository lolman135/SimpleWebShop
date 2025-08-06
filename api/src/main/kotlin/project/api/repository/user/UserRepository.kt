package project.api.repository.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import project.api.entity.User
import java.util.UUID

@Repository
interface UserRepository: JpaRepository<User, UUID> {
}