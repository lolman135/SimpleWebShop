package project.api.service.business.user

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import project.api.dto.request.auth.RegisterRequest
import project.api.dto.request.business.UserDtoRequest
import project.api.dto.response.business.UserDtoResponse
import project.api.entity.User
import project.api.exception.EntityNotFoundException
import project.api.exception.UserAlreadyExistsException
import project.api.mapper.auth.toUser
import project.api.mapper.business.user.UserMapper
import project.api.repository.user.UserRepository
import project.api.service.business.role.RoleService
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val roleService: RoleService
) : UserService {

    override fun deleteById(id: UUID): Boolean {
        if (!userRepository.existsById(id))
            throw EntityNotFoundException("User with id=$id not found")

        userRepository.deleteById(id)
        return true
    }

    @Transactional
    override fun save(request: RegisterRequest): User {
        if (userRepository.existsUserByUsername(request.username))
            throw UserAlreadyExistsException("User with username ${request.username} already exists")

        if (userRepository.existsUserByEmail(request.email))
            throw UserAlreadyExistsException("User with email ${request.email} already exists")

        val user = request.toUser()
        user.roles = mutableSetOf(roleService.getDefaultRole())
        return userRepository.save(user)
    }

    @Transactional
    override fun findAll(): List<UserDtoResponse> = userRepository.findAll().map { userMapper.toDto(it) }

    @Transactional
    override fun findById(id: UUID): UserDtoResponse {
        val user = userRepository.findById(id)
            .orElseThrow { EntityNotFoundException("User with id=$id not found") }
        return userMapper.toDto(user)
    }

    @Transactional
    override fun updateById(id: UUID, dto: UserDtoRequest): UserDtoResponse {
        if (!userRepository.existsById(id))
            throw EntityNotFoundException("User with id=$id not found")

        val user = userMapper.toUser(dto)
        user.id = id
        val updatedUser = userRepository.save(user)
        return userMapper.toDto(updatedUser)
    }

    @Transactional
    override fun findByUsername(username: String): UserDtoResponse{
        val user = userRepository.findUserByUsername(username)
            .orElseThrow { EntityNotFoundException("User with username=$username not found") }
        return userMapper.toDto(user)
    }
}