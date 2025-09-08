package project.api.service.business.user

import project.api.dto.request.auth.RegisterRequest
import project.api.dto.request.business.UserDtoRequest
import project.api.entity.User
import java.util.*

interface UserService {
    fun save(request: RegisterRequest): User
    fun findAll(): List<User>
    fun findById(id: UUID): User
    fun updateById(id: UUID, dto: UserDtoRequest): User
    fun deleteById(id: UUID): Boolean
    fun findByUsername(username: String): User
}