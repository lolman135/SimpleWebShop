package project.api.service.business.user

import jakarta.transaction.Transactional
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import project.api.dto.request.authentication.RegisterRequest
import project.api.dto.request.business.UserDtoUpdateMeRequest
import project.api.dto.request.business.UserDtoUpdateRequest
import project.api.dto.response.business.UserDtoResponse
import project.api.entity.User
import project.api.exception.EntityNotFoundException
import project.api.exception.EntityAlreadyExistsException
import project.api.mapper.authentication.toUser
import project.api.mapper.business.user.UserMapper
import project.api.repository.feedback.FeedbackRepository
import project.api.repository.order.OrderRepository
import project.api.repository.role.RoleRepository
import project.api.repository.user.UserRepository
import project.api.service.business.role.RoleService
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val roleService: RoleService,
    private val passwordEncoder: PasswordEncoder,
    private val roleRepository: RoleRepository,
    private val orderRepository: OrderRepository,
    private val feedbackRepository: FeedbackRepository,
) : UserService {

    @Caching(
        evict = [
            CacheEvict(value = ["users"], key = "#id"),
            CacheEvict(value = ["userList"], allEntries = true)
        ]
    )
    override fun deleteById(id: UUID): Boolean {
        if (!userRepository.existsById(id))
            throw EntityNotFoundException("User with id=$id not found")

        userRepository.deleteById(id)
        return true
    }

    @Transactional
    @CacheEvict(value = ["userList"], allEntries = true)
    override fun save(request: RegisterRequest): User {
        if (userRepository.existsUserByUsername(request.username))
            throw EntityAlreadyExistsException("User with username ${request.username} already exists")

        if (userRepository.existsUserByEmail(request.email))
            throw EntityAlreadyExistsException("User with email ${request.email} already exists")

        val user = request.toUser()
        user.roles = mutableSetOf(roleService.getDefaultRole())
        return userRepository.save(user)
    }

    @Transactional
    @Cacheable(value = ["userList"])
    override fun findAll(): List<UserDtoResponse> = userRepository.findAll().map { userMapper.toDto(it) }

    @Transactional
    @Cacheable(value = ["users"], key = "#id")
    override fun findById(id: UUID): UserDtoResponse {
        val user = findRawUserById(id)
        return userMapper.toDto(user)
    }

    @Transactional
    @Caching(
        put = [CachePut(value = ["users"], key = "#id")],
        evict = [CacheEvict(value = ["userList"], allEntries = true)]
    )
    override fun updateById(id: UUID, request: UserDtoUpdateRequest): UserDtoResponse {
        val user = findRawUserById(id)

        updateBasicUserFields(user, request.username, request.password, request.email)
        request.roleIds?.let { user.roles = it.map{
            id -> roleRepository.findById(id)
                .orElseThrow { EntityNotFoundException("Role with id=$id not found") }
        }.toMutableSet()}

        request.orderIds?.let {user.orders = it.map{
            id -> orderRepository.findById(id)
                .orElseThrow { EntityNotFoundException("Order with id=$id not found") }
        }.toMutableSet()}

        request.feedbackIds?.let{user.feedbacks = it.map{
            id -> feedbackRepository.findById(id)
                .orElseThrow { EntityNotFoundException("Feedback with id=$id not found") }
        }.toMutableSet()}

        val updatedUser = userRepository.save(user)
        return userMapper.toDto(updatedUser)
    }

    @Transactional
    @Caching(
        put = [CachePut(value = ["users"], key = "#id")],
        evict = [CacheEvict(value = ["userList"], allEntries = true)]
    )
    override fun updateMeById(id: UUID, request: UserDtoUpdateMeRequest): UserDtoResponse {
        val user = findRawUserById(id)

        updateBasicUserFields(user, request.username, request.password, request.email)
        val updatedUser = userRepository.save(user)
        return userMapper.toDto(updatedUser)
    }

    @Transactional
    override fun findByUsername(username: String): UserDtoResponse{
        val user = userRepository.findUserByUsername(username)
            .orElseThrow { EntityNotFoundException("User with username=$username not found") }
        return userMapper.toDto(user)
    }

    @Transactional
    @Cacheable(value = ["users"], key = "#user.id")
    override fun findMe(user: User): UserDtoResponse {
        return userMapper.toDto(user)
    }

    @Transactional
    override fun findRawUserById(id: UUID): User = userRepository.findById(id)
        .orElseThrow {EntityNotFoundException("User with id=$id not found")}

    @Transactional
    override fun findAllUsersByUsernamePrefix(prefix: String) =
        userRepository.findUsersByUsernamePrefix(prefix).map{userMapper.toDto(it)}


    private fun updateBasicUserFields(user: User, username: String?, password: String?, email: String?){
        username?.let { user.username = it }
        password?.let { user.password = passwordEncoder.encode(it) }
        email?.let { user.email = it }
    }
}