import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import project.api.dto.request.business.RoleDtoRequest
import project.api.entity.Role
import project.api.mapper.business.role.RoleMapperImpl
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RoleMapperTest {

    private lateinit var roleDtoRequest: RoleDtoRequest
    private lateinit var roleMapper: RoleMapperImpl

    @BeforeEach
    fun setUp() {
        roleDtoRequest = RoleDtoRequest("USER")
        roleMapper = RoleMapperImpl()
    }

    @Test
    fun toRoleShouldReturnCorrectRoleName() {
        val role = roleMapper.toRole(roleDtoRequest)
        assertEquals("USER", role.name)
    }

    @Test
    fun toDtoShouldMapCorrectly() {
        val roleId = UUID.randomUUID()
        val role = Role(id = roleId, name = "ADMIN")

        val dto = roleMapper.toDto(role)

        assertEquals(roleId, dto.id)
        assertEquals("ADMIN", dto.name)
    }

    @Test
    fun toDtoShouldThrowExceptionWhenIdIsNull() {
        val role = Role(id = null, name = "NO_ID_ROLE")

        assertFailsWith<IllegalArgumentException> {
            roleMapper.toDto(role)
        }
    }
}