package project.api.unit.security

import project.api.repository.user.UserRepository

import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.core.userdetails.UsernameNotFoundException
import project.api.entity.User
import project.api.security.CustomUserDetailService
import project.api.security.CustomUserDetails
import java.util.*

@ExtendWith(MockitoExtension::class)
class CustomUserDetailServiceTest {

    @Mock
    lateinit var userRepository: UserRepository

    @InjectMocks
    lateinit var service: CustomUserDetailService

    private lateinit var existingUser: User
    private lateinit var userId: UUID
    private val username = "testUser"

    @BeforeEach
    fun setUp() {
        userId = UUID.randomUUID()
        existingUser = User(username = username, email = "a@b.com", password = "123")
    }

    @Test
    fun loadUserByUsernameReturnsUserDetailsWhenUserExists() {
        `when`(userRepository.findUserByUsername(username)).thenReturn(Optional.of(existingUser))

        val userDetails = service.loadUserByUsername(username)

        Assertions.assertNotNull(userDetails)
        Assertions.assertTrue(userDetails is CustomUserDetails)
        Assertions.assertEquals(username, userDetails.username)
    }

    @Test
    fun loadUserByUsernameThrowsWhenUsernameIsNull() {
        Assertions.assertThrows(UsernameNotFoundException::class.java) {
            service.loadUserByUsername(null)
        }
    }

    @Test
    fun loadUserByUsernameThrowsWhenUserNotFound() {
        `when`(userRepository.findUserByUsername(username)).thenReturn(Optional.empty())

        Assertions.assertThrows(UsernameNotFoundException::class.java) {
            service.loadUserByUsername(username)
        }
    }

    @Test
    fun loadUserByIdReturnsUserDetailsWhenUserExists() {
        `when`(userRepository.findById(userId)).thenReturn(Optional.of(existingUser))

        val userDetails = service.loadUserById(userId)

        Assertions.assertNotNull(userDetails)
        Assertions.assertTrue(userDetails is CustomUserDetails)
        Assertions.assertEquals(existingUser.username, userDetails.username)
    }

    @Test
    fun loadUserByIdThrowsWhenUserNotFound() {
        `when`(userRepository.findById(userId)).thenReturn(Optional.empty())

        Assertions.assertThrows(UsernameNotFoundException::class.java) {
            service.loadUserById(userId)
        }
    }
}
