package project.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain{
        http {
            authorizeHttpRequests {
                authorize(anyRequest, permitAll)
            }
            formLogin {  }
            httpBasic {  }
        }

        return http.build()
    }
}