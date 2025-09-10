package project.api.repository.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import project.api.entity.User
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID> {
     fun findUserByUsername(username: String): Optional<User>
     fun existsUserByUsername(username: String): Boolean
     fun existsUserByEmail(email: String): Boolean

     @Query("SELECT u FROM User u WHERE u.username LIKE CONCAT(:prefix, '%')")
     fun findUsersByUsernamePrefix(@Param("prefix") prefix: String): List<User>
}