package project.api.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import project.api.dto.response.error.ErrorResponse
import java.time.LocalDateTime

@Component
class JsonAccessDeniedHandler(
    private val objectMapper: ObjectMapper
) : AccessDeniedHandler{

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        response.status = HttpServletResponse.SC_FORBIDDEN
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        val body = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpServletResponse.SC_FORBIDDEN,
            error = "Forbidden",
            message = accessDeniedException.message,
            path = request.servletPath
        )
        response.writer.write(objectMapper.writeValueAsString(body))
    }
}