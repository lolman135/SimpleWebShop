package project.api.service.business.user

import project.api.dto.request.authentication.RegisterRequest
import project.api.dto.request.business.UserDtoUpdateMeRequest
import project.api.dto.request.business.UserDtoUpdateRequest
import project.api.dto.response.business.UserDtoResponse
import project.api.entity.User
import java.util.*

interface UserService {
    fun save(request: RegisterRequest): User
    fun findAll(): List<UserDtoResponse>
    fun findById(id: UUID): UserDtoResponse
    fun updateById(id: UUID, request: UserDtoUpdateRequest): UserDtoResponse
    fun deleteById(id: UUID): Boolean
    fun findByUsername(username: String): UserDtoResponse
    fun findMe(user: User): UserDtoResponse
    fun findRawUserById(id: UUID): User
    fun findAllUsersByUsernamePrefix(prefix: String): List<UserDtoResponse>
    fun updateMeById(id: UUID, request: UserDtoUpdateMeRequest): UserDtoResponse
}