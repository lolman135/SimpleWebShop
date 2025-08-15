package project.api.service.business.user

import project.api.dto.UserDto
import project.api.entity.User
import java.util.*

interface UserService {
    fun save(dto: UserDto): User
    fun findAll(): List<User>
    fun findById(): User
    fun updateById(id: UUID, dto: UserDto): User
    fun deleteById(id: UUID): Boolean
}