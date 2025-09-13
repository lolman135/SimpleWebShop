package project.api.controller.business

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import project.api.dto.request.business.UserDtoUpdateRequest
import project.api.dto.response.business.UserDtoResponse
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

    @GetMapping("/username")
    fun getUsersByUsernamePrefix(@RequestParam prefix: String) =
        ResponseEntity.ok(userService.findAllUsersByUsernamePrefix(prefix))

    @GetMapping
    fun getAllUsers() = ResponseEntity.ok(userService.findAll())

    @PutMapping("/{id}")
    fun updateUserById(@PathVariable id: UUID, @RequestBody @Valid request: UserDtoUpdateRequest) =
        ResponseEntity.ok(userService.updateById(id, request))

    @PutMapping("/me")
    fun updateMe(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @RequestBody @Valid request: UserDtoUpdateRequest
    ): ResponseEntity<UserDtoResponse> {
        val id = userDetails.getId()
        return ResponseEntity.ok(userService.updateById(id!!, request))
    }

    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable id: UUID): ResponseEntity<Void>{
        userService.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/me")
    fun deleteMe(@AuthenticationPrincipal userDetails: CustomUserDetails): ResponseEntity<Void> {
        val id = userDetails.getId()
        userService.deleteById(id!!)
        return ResponseEntity.noContent().build()
    }
}