package project.api.service.business.user

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import project.api.dto.request.auth.RegisterRequest
import project.api.dto.request.business.UserDtoRequest
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
    override fun findAll(): List<User> = userRepository.findAll()

    @Transactional
    override fun findById(id: UUID): User = userRepository.findById(id)
        .orElseThrow { EntityNotFoundException("User with id=$id not found") }

    @Transactional
    override fun updateById(id: UUID, dto: UserDtoRequest): User {
        if (!userRepository.existsById(id))
            throw EntityNotFoundException("User with id=$id not found")

        val user = userMapper.toUser(dto)
        user.id = id
        return userRepository.save(user)
    }

    @Transactional
    override fun findByUsername(username: String): User = userRepository.findUserByUsername(username)
        .orElseThrow { EntityNotFoundException("User with username=$username not found") }
}