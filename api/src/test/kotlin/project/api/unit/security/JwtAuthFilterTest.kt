package project.api.unit.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import project.api.security.CustomUserDetailService
import project.api.security.JwtAuthFilter
import project.api.security.JwtTokenProvider
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class JwtAuthFilterTest {

    @Mock lateinit var jwtTokenProvider: JwtTokenProvider
    @Mock lateinit var customUserDetailService: CustomUserDetailService

    @InjectMocks lateinit var jwtAuthFilter: JwtAuthFilter

    private lateinit var request: HttpServletRequest
    private lateinit var response: HttpServletResponse
    private lateinit var filterChain: FilterChain
    private lateinit var token: String

    @BeforeEach
    fun setUp() {
        SecurityContextHolder.clearContext()

        request = mock(HttpServletRequest::class.java)
        response = mock(HttpServletResponse::class.java)
        filterChain = mock(FilterChain::class.java)
        token = "valid.jwt.token"

    }

    @AfterEach
    fun clearContext() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun doFilterSetsAuthenticationWhenValidToken() {
        val userId = UUID.randomUUID()

        `when`(request.getHeader("Authorization")).thenReturn("Bearer $token")
        `when`(jwtTokenProvider.validateToken(token)).thenReturn(true)
        `when`(jwtTokenProvider.getUserIdFromToken(token)).thenReturn(userId)

        val userDetails: UserDetails = User("testuser", "password", listOf())
        `when`(customUserDetailService.loadUserById(userId)).thenReturn(userDetails)

        jwtAuthFilter.doFilter(request, response, filterChain)

        val auth = SecurityContextHolder.getContext().authentication
        assertNotNull(auth)
        assertEquals(userDetails.username, (auth!!.principal as UserDetails).username)

        verify(filterChain).doFilter(request, response)
    }

    @Test
    fun doFilterDoesNotSetAuthenticationWhenNoToken() {
        `when`(request.getHeader("Authorization")).thenReturn(null)

        jwtAuthFilter.doFilter(request, response, filterChain)

        assertNull(SecurityContextHolder.getContext().authentication)
        verify(filterChain).doFilter(request, response)
    }

    @Test
    fun doFilterDoesNotSetAuthenticationWhenTokenInvalid() {
        `when`(request.getHeader("Authorization")).thenReturn("Bearer $token")
        `when`(jwtTokenProvider.validateToken(token)).thenReturn(false)

        jwtAuthFilter.doFilter(request, response, filterChain)

        assertNull(SecurityContextHolder.getContext().authentication)
        verify(filterChain).doFilter(request, response)
    }
}
