package project.api.controller.business

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import project.api.dto.request.business.UserDtoUpdateRequest
import project.api.service.business.user.UserService
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users")
class UserController(private val userService: UserService) {

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id:UUID) = ResponseEntity.ok(userService.findById(id))

    @GetMapping
    fun getAllUsers() = ResponseEntity.ok(userService.findAll())

    @GetMapping("/find")
    fun getUserByName(@RequestParam username: String) = ResponseEntity.ok(userService.findByUsername(username))

    @PutMapping("/{id}")
    fun updateUserById(@PathVariable id: UUID, @RequestParam request: UserDtoUpdateRequest) =
        ResponseEntity.ok(userService.updateById(id, request))

    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable id:UUID) = ResponseEntity.ok(userService.deleteById(id))
}