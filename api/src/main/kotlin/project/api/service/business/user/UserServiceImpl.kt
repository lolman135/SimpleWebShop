package project.api.service.business.user

import org.springframework.stereotype.Service
import project.api.dto.business.UserDto
import project.api.entity.User
import project.api.exception.EntityNotFoundException
import project.api.mapper.business.user.UserMapper
import project.api.repository.user.UserRepository
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) : UserService {

    override fun deleteById(id: UUID): Boolean {
        if (!userRepository.existsById(id))
            throw EntityNotFoundException("User with id=$id not found")

        userRepository.deleteById(id)
        return true
    }

    override fun save(dto: UserDto): User {
        val user = userMapper.toUser(dto)
        return userRepository.save(user)
    }

    override fun findAll(): List<User> = userRepository.findAll()

    override fun findById(id: UUID): User = userRepository.findById(id).orElseThrow {
        EntityNotFoundException("User with id=$id not found")
    }

    override fun updateById(id: UUID, dto: UserDto): User {
        if (!userRepository.existsById(id))
            throw EntityNotFoundException("User with id=$id not found")

        val user = userMapper.toUser(dto)
        user.id = id
        return userRepository.save(user)
    }
}