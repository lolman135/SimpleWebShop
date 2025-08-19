package project.api.unit.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import project.api.dto.UserDto
import project.api.entity.User
import project.api.exception.EntityNotFoundException
import project.api.mapper.user.UserMapper
import project.api.repository.user.UserRepository
import project.api.service.business.user.UserServiceImpl
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository
    @Mock
    private lateinit var userMapper: UserMapper

    @InjectMocks
    private lateinit var userService: UserServiceImpl

    private lateinit var userId: UUID
    private lateinit var userDto: UserDto
    private lateinit var user: User

    @BeforeEach
    fun setUp() {
        userId = UUID.randomUUID()
        userDto = UserDto(
            username = "JohnDoe",
            email = "john@example.com",
            password = "securePass123",
        )
        user = User(
            id = userId,
            username = "JohnDoe",
            email = "john@example.com",
            password = "securePass123",
        )
    }

    @Test
    fun saveShouldMapDtoToUserAndSaveEntity() {
        `when`(userMapper.toUser(userDto)).thenReturn(user)
        `when`(userRepository.save(user)).thenReturn(user)

        val result = userService.save(userDto)

        assertEquals(user, result)
        verify(userMapper).toUser(userDto)
        verify(userRepository).save(user)
    }

    @Test
    fun findAllShouldReturnListOfUsers() {
        val users = listOf(user)
        `when`(userRepository.findAll()).thenReturn(users)

        val result = userService.findAll()

        assertEquals(users, result)
        verify(userRepository).findAll()
    }

    @Test
    fun findByIdShouldReturnUserWhenExists() {
        `when`(userRepository.findById(userId)).thenReturn(Optional.of(user))

        val result = userService.findById(userId)

        assertEquals(user, result)
        verify(userRepository).findById(userId)
    }

    @Test
    fun findByIdShouldThrowExceptionWhenUserDoesNotExist() {
        `when`(userRepository.findById(userId)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            userService.findById(userId)
        }
        verify(userRepository).findById(userId)
    }

    @Test
    fun updateByIdShouldUpdateUserWhenExists() {
        `when`(userRepository.existsById(userId)).thenReturn(true)
        `when`(userMapper.toUser(userDto)).thenReturn(user)
        `when`(userRepository.save(user)).thenReturn(user)

        val result = userService.updateById(userId, userDto)

        assertEquals(user, result)
        verify(userRepository).existsById(userId)
        verify(userMapper).toUser(userDto)
        verify(userRepository).save(user)
    }

    @Test
    fun updateByIdShouldThrowExceptionWhenUserDoesNotExist() {
        `when`(userRepository.existsById(userId)).thenReturn(false)

        assertThrows<EntityNotFoundException> {
            userService.updateById(userId, userDto)
        }
        verify(userRepository).existsById(userId)
        verify(userMapper, never()).toUser(userDto)
        verify(userRepository, never()).save(any(User::class.java))
    }

    @Test
    fun deleteByIdShouldDeleteUserWhenExists() {
        `when`(userRepository.existsById(userId)).thenReturn(true)

        val result = userService.deleteById(userId)

        assertTrue(result)
        verify(userRepository).existsById(userId)
        verify(userRepository).deleteById(userId)
    }

    @Test
    fun deleteByIdShouldThrowExceptionWhenUserDoesNotExist() {
        `when`(userRepository.existsById(userId)).thenReturn(false)

        assertThrows<EntityNotFoundException> {
            userService.deleteById(userId)
        }
        verify(userRepository).existsById(userId)
        verify(userRepository, never()).deleteById(userId)
    }

}