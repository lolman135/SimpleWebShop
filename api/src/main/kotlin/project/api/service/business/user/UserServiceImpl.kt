package project.api.service.business.user

import org.springframework.stereotype.Service
import project.api.dto.auth.RegisterRequest
import project.api.dto.business.UserDto
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

    override fun save(request: RegisterRequest): User {
        if (userRepository.existsUserByUsername(request.username))
            throw UserAlreadyExistsException("User with username ${request.username} already exists")

        if (userRepository.existsUserByEmail(request.email))
            throw UserAlreadyExistsException("User with email ${request.email} already exists")

        val user = request.toUser()
        user.roles = mutableSetOf(roleService.getDefaultRole())
        return userRepository.save(user)
    }

    override fun findAll(): List<User> = userRepository.findAll()

    override fun findById(id: UUID): User = userRepository.findById(id)
        .orElseThrow { EntityNotFoundException("User with id=$id not found") }

    override fun updateById(id: UUID, dto: UserDto): User {
        if (!userRepository.existsById(id))
            throw EntityNotFoundException("User with id=$id not found")

        val user = userMapper.toUser(dto)
        user.id = id
        return userRepository.save(user)
    }

    override fun findByUsername(username: String): User = userRepository.findUserByUsername(username)
        .orElseThrow { EntityNotFoundException("User with username=$username not found") }
}