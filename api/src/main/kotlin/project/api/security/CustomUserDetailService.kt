package project.api.security

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import project.api.repository.user.UserRepository

@Service
class CustomUserDetailService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) throw UsernameNotFoundException("Invalid name provided")

        val user = userRepository.findUserByUsername(username)
            .orElseThrow { UsernameNotFoundException("User with username=$username not found") }

        val authorities = user.roles.map { SimpleGrantedAuthority(it.name) }

        //This User class from rg.springframework.security.core, not my custom User entity
        return User(user.username, user.password, authorities)
    }
}