package project.api.unit.security

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import project.api.security.JwtTokenProvider
import java.util.UUID
import java.util.concurrent.TimeUnit

class JwtTokenProviderTest {

    private val secret = "01234567890123456789012345678901"
    private val expirationMs = 1000L * 60

    private val jwtTokenProvider = JwtTokenProvider(secret, expirationMs)

    @Test
    fun generateTokenCreatesValidTokenAndAllowsExtractingUserId() {
        val userId = UUID.randomUUID()

        val token = jwtTokenProvider.generateToken(userId)

        assertNotNull(token)
        assertTrue(jwtTokenProvider.validateToken(token))

        val extractedId = jwtTokenProvider.getUserIdFromToken(token)
        assertEquals(userId, extractedId)

        val subject = jwtTokenProvider.getUsernameFromToken(token)
        assertEquals(userId.toString(), subject)
    }

    @Test
    fun validateTokenReturnsFalseForInvalidToken() {
        val invalidToken = "not.a.jwt.token"
        assertFalse(jwtTokenProvider.validateToken(invalidToken))
    }

    @Test
    fun validateTokenReturnsFalseForExpiredToken() {
        val shortLivedProvider = JwtTokenProvider(secret, 1)
        val userId = UUID.randomUUID()

        val token = shortLivedProvider.generateToken(userId)

        TimeUnit.MILLISECONDS.sleep(5)

        assertFalse(shortLivedProvider.validateToken(token))
    }

    @Test
    fun getUserIdFromTokenThrowsForInvalidToken() {
        val invalidToken = "bad.token"
        assertThrows(Exception::class.java) {
            jwtTokenProvider.getUserIdFromToken(invalidToken)
        }
    }
}