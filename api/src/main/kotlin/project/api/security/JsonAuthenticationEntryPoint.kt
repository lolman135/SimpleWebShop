package project.api.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import project.api.dto.response.error.ErrorResponse
import java.time.LocalDateTime

@Component
class JsonAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        val body = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpServletResponse.SC_UNAUTHORIZED,
            error = "Unauthorized",
            message = authException.message,
            path = request.servletPath
        )
        response.writer.write(objectMapper.writeValueAsString(body))
    }
}