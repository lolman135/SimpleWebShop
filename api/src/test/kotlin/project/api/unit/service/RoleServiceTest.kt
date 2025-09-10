package project.api.unit.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import project.api.dto.request.business.RoleDtoRequest
import project.api.dto.response.business.OrderDtoResponse
import project.api.dto.response.business.RoleDtoResponse
import project.api.entity.Role
import project.api.exception.EntityNotFoundException
import project.api.mapper.business.role.RoleMapper
import project.api.repository.role.RoleRepository
import project.api.service.business.role.RoleServiceImpl
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class RoleServiceTest {

    @Mock private lateinit var roleRepository: RoleRepository
    @Mock private lateinit var roleMapper: RoleMapper

    @InjectMocks private lateinit var roleService: RoleServiceImpl

    private lateinit var roleId: UUID
    private lateinit var roleDtoRequest: RoleDtoRequest
    private lateinit var role: Role
    private lateinit var roleDtoResponse: RoleDtoResponse

    @BeforeEach
    fun setUp() {
        roleId = UUID.randomUUID()
        roleDtoRequest = RoleDtoRequest(name = "TEST_ROLE")
        role = Role(id = roleId, name = "TEST_ROLE")
        roleDtoResponse = RoleDtoResponse(id = roleId, name = "TEST_ROLE")
    }

    @Test
    fun saveShouldMapDtoToRoleAndSaveEntity() {
        `when`(roleMapper.toRole(roleDtoRequest)).thenReturn(role)
        `when`(roleMapper.toDto(role)).thenReturn(roleDtoResponse)
        `when`(roleRepository.save(role)).thenReturn(role)

        val savedRole = roleService.save(roleDtoRequest)
        assertEquals(roleDtoResponse, savedRole)
        verify(roleMapper).toRole(roleDtoRequest)

        verify(roleMapper).toRole(roleDtoRequest)
        verify(roleMapper).toDto(role)
        verify(roleRepository).save(role)
    }

    @Test
    fun findAllShouldReturnListOfRoles() {
        val roles = listOf(role)
        val responseList = listOf(roleDtoResponse)
        `when`(roleRepository.findAll()).thenReturn(roles)
        `when`(roleMapper.toDto(role)).thenReturn(roleDtoResponse)

        val result = roleService.findAll()
        assertEquals(responseList, result)
        verify(roleRepository).findAll()
    }

    @Test
    fun findByIdShouldReturnRoleIfExist() {
        `when`(roleRepository.findById(roleId)).thenReturn(Optional.of(role))
        `when`(roleMapper.toDto(role)).thenReturn(roleDtoResponse)
        val result = roleService.findById(roleId)

        assertEquals(roleDtoResponse, result)
        verify(roleRepository).findById(roleId)
        verify(roleMapper).toDto(role)
    }

    @Test
    fun findByIdShouldThrowExceptionWhenRoleDoesNotExist() {
        `when`(roleRepository.findById(roleId)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> {
            roleService.findById(roleId)
        }
        verify(roleRepository).findById(roleId)
    }

    @Test
    fun updateByIdShouldUpdateRoleWhenExists() {
        `when`(roleRepository.existsById(roleId)).thenReturn(true)
        `when`(roleMapper.toRole(roleDtoRequest)).thenReturn(role)
        `when`(roleMapper.toDto(role)).thenReturn(roleDtoResponse)
        `when`(roleRepository.save(role)).thenReturn(role)

        val result = roleService.updateById(roleId, roleDtoRequest)

        Assertions.assertEquals(roleDtoResponse, result)
        verify(roleRepository).existsById(roleId)
        verify(roleMapper).toRole(roleDtoRequest)
        verify(roleMapper).toDto(role)
        verify(roleRepository).save(role)
    }

    @Test
    fun updateByIdShouldThrowExceptionWhenRoleDoesNotExist() {
        `when`(roleRepository.existsById(roleId)).thenReturn(false)

        assertThrows<EntityNotFoundException> {
            roleService.updateById(roleId, roleDtoRequest)
        }
        verify(roleRepository).existsById(roleId)
        verify(roleMapper, never()).toRole(roleDtoRequest)
        verify(roleMapper, never()).toDto(role)
        verify(roleRepository, never()).save(any(Role::class.java))
    }

    @Test
    fun deleteByIdShouldDeleteRoleWhenExists() {
        `when`(roleRepository.existsById(roleId)).thenReturn(true)

        val result = roleService.deleteById(roleId)

        assertTrue(result)
        verify(roleRepository).existsById(roleId)
        verify(roleRepository).deleteById(roleId)
    }

    @Test
    fun deleteByIdShouldThrowExceptionWhenRoleDoesNotExists() {
        `when`(roleRepository.existsById(roleId)).thenReturn(false)

        assertThrows<EntityNotFoundException> {
            roleService.deleteById(roleId)
        }
        verify(roleRepository).existsById(roleId)
        verify(roleRepository, never()).deleteById(roleId)
    }
}