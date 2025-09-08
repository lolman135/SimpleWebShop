package project.api.unit.mappers

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import project.api.dto.request.business.RoleDtoRequest
import project.api.mapper.business.role.RoleMapper
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@Suppress("UNREACHABLE_CODE")
@SpringBootTest
class RoleMapperTest {

    private lateinit var roleDtoRequest: RoleDtoRequest

    @Autowired
    private lateinit var roleMapper: RoleMapper

    @BeforeEach
    fun setUp(){
        roleDtoRequest = RoleDtoRequest(
            name = "USER"
        )
    }

    @Test
    fun toRoleShouldReturnCorrectRoleName(){
        var role = roleMapper.toRole(roleDtoRequest)

        assertEquals("USER", roleDtoRequest.name)
    }

    @Test
    fun toRoleShouldThrowException(){
        assertFailsWith<Exception> {
            roleMapper.toRole(null!!)
        }
    }

}