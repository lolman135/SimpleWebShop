package project.api.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
    private val userDetailService: CustomUserDetailService
){

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain{

        http {
            csrf { disable() }
            authorizeHttpRequests {
                authorize("/api/v1/auth/*", permitAll)
                authorize(anyRequest, authenticated)
            }
            sessionManagement {
                sessionCreationPolicy = org.springframework.security.config.http.SessionCreationPolicy.STATELESS
            }
            addFilterBefore<org.springframework.security.web
                .authentication.UsernamePasswordAuthenticationFilter>(jwtAuthFilter)
        }

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager =
        authConfig.authenticationManager
}