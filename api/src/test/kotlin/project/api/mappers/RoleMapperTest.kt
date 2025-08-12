package project.api.mappers

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
    fun testRoleMapperShouldReturnCorrectRoleName(){
        var role = roleMapper.mapToRole(roleDto)

        assertEquals("USER", roleDto.name)
    }

    @Test
    fun testRoleMapperShouldThrowException(){
        assertFailsWith<Exception> {
            roleMapper.mapToRole(null!!)
        }
    }

}