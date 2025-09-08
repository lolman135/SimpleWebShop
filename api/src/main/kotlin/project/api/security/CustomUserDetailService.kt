package project.api.security

import jakarta.transaction.Transactional
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import project.api.repository.user.UserRepository

@Service
class CustomUserDetailService(
    private val userRepository: UserRepository
) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) throw UsernameNotFoundException("Invalid name provided")

        val user = userRepository.findUserByUsername(username)
            .orElseThrow { UsernameNotFoundException("User with username $username not found") }
        return CustomUserDetails(user)
    }
}