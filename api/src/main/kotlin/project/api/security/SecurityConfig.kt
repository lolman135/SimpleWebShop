package project.api.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
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
                //Authentication
                authorize("/api/v1/auth/*", permitAll)
                //Products
                authorize(HttpMethod.GET ,"/api/v1/products/**", permitAll)
                authorize("/api/v1/products/**", hasRole("ADMIN"))
                //Categories
                authorize(HttpMethod.GET ,"/api/v1/categories/**", permitAll)
                authorize("/api/v1/categories/**", hasRole("ADMIN"))
                //Users
                authorize("/api/v1/users/me", hasAnyRole("USER", "ADMIN"))
                authorize("/api/v1/users/**", hasRole("ADMIN"))
                //Roles
                authorize("/api/v1/roles/**", hasRole("ADMIN"))
                //Order
                authorize(HttpMethod.POST,  "/api/v1/orders/**", hasAnyRole("USER", "ADMIN"))
                authorize(HttpMethod.GET, "/api/v1/orders/me", hasAnyRole("USER", "ADMIN"))
                authorize(HttpMethod.GET, "/api/v1/orders/**", hasRole("ADMIN"))
                authorize(HttpMethod.PUT, "/api/v1/orders/**", hasAnyRole("USER", "ADMIN"))
                authorize(HttpMethod.DELETE, "/api/v1/orders/**", hasAnyRole("USER", "ADMIN"))
                //Feedback
                authorize("/api/v1/feedbacks/**", hasAnyRole("USER", "ADMIN"))
                //Other requests
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