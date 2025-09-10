package project.api.controller.business

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import project.api.dto.request.business.UserDtoUpdateRequest
import project.api.security.CustomUserDetails
import project.api.service.business.user.UserService
import java.util.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(private val userService: UserService) {

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: UUID) = ResponseEntity.ok(userService.findById(id))

    @GetMapping("/me")
    fun getMe(@AuthenticationPrincipal userDetails: CustomUserDetails) =
        ResponseEntity.ok(userService.findById(userDetails.getId()!!))

    @GetMapping
    fun getAllUsers() = ResponseEntity.ok(userService.findAll())

    @GetMapping("/find")
    fun getUserByName(@RequestParam username: String) = ResponseEntity.ok(userService.findByUsername(username))

    @PutMapping("/{id}")
    fun updateUserById(@PathVariable id: UUID, @RequestBody @Valid request: UserDtoUpdateRequest) =
        ResponseEntity.ok(userService.updateById(id, request))

    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable id: UUID) = ResponseEntity.ok(userService.deleteById(id))

    @GetMapping("/username")
    fun getUsersByUsernamePrefix(@RequestParam prefix: String) =
        ResponseEntity.ok(userService.findAllUsersByUsernamePrefix(prefix))
}