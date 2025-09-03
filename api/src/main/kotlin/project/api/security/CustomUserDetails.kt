package project.api.security

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import project.api.entity.User

class CustomUserDetails(val user: User) : UserDetails{
    override fun getAuthorities() = user.roles.map{ SimpleGrantedAuthority(it.name) }
    override fun getPassword() = user.password
    override fun getUsername() = user.username
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
    override fun isAccountNonExpired() = true
}