package project.api.unit.mappers

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import project.api.dto.RoleDto
import project.api.mapper.role.RoleMapper
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@Suppress("UNREACHABLE_CODE")
@SpringBootTest
class RoleMapperTest {

    lateinit var roleDto: RoleDto

    @Autowired
    lateinit var roleMapper: RoleMapper

    @BeforeEach
    fun setUp(){
        roleDto = RoleDto(
            name = "USER"
        )
    }

    @Test
    fun toRoleShouldReturnCorrectRoleName(){
        var role = roleMapper.toRole(roleDto)

        assertEquals("USER", roleDto.name)
    }

    @Test
    fun toRoleShouldThrowException(){
        assertFailsWith<Exception> {
            roleMapper.toRole(null!!)
        }
    }

}