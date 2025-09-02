package project.api.controller.business

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import project.api.dto.business.UserDto
import project.api.entity.User
import project.api.service.business.user.UserService
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users")
class UserController(private val userService: UserService, ) {

    @GetMapping("/{id}")
    fun getById(@PathVariable id:UUID) = ResponseEntity.ok(userService.findById(id))

    @GetMapping
    fun getAll() = ResponseEntity.ok(userService.findAll())

    @GetMapping("/find")
    fun getByName(@RequestParam username: String) = ResponseEntity.ok(userService.findByUsername(username))

    @PutMapping("/{id}")
    fun updateById(@PathVariable id: UUID, @RequestParam request: UserDto) =
        ResponseEntity.ok(userService.updateById(id, request))

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id:UUID) = ResponseEntity.ok(userService.deleteById(id))
}