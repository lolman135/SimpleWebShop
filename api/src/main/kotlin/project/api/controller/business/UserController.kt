package project.api.controller.business

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import project.api.dto.request.business.UserDtoUpdateMeRequest
import project.api.dto.request.business.UserDtoUpdateRequest
import project.api.dto.response.business.UserDtoResponse
import project.api.dto.response.error.ErrorResponse
import project.api.security.CustomUserDetails
import project.api.service.business.user.UserService
import java.util.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(private val userService: UserService) {

    @GetMapping("/{id}")
    @Operation(summary = "Returns user", description = "Returns user by id if exists")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully returns user",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = UserDtoResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    fun getUserById(@PathVariable id: UUID) =
        ResponseEntity.ok(userService.findById(id))

    @GetMapping("/me")
    @Operation(summary = "Returns current user", description = "Returns user from access token")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully returns user",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = UserDtoResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    fun getMe(@AuthenticationPrincipal userDetails: CustomUserDetails) =
        ResponseEntity.ok(userService.findById(userDetails.getId()!!))

    @GetMapping("/username")
    @Operation(
        summary = "Returns all users by username prefix",
        description = "Returns all users by username prefix as list. Returns empty list if none found"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully returns list of users",
                content = [Content(
                    mediaType = "application/json",
                    array = io.swagger.v3.oas.annotations.media.ArraySchema(
                        schema = Schema(implementation = UserDtoResponse::class)
                    )
                )]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    fun getUsersByUsernamePrefix(@RequestParam prefix: String) =
        ResponseEntity.ok(userService.findAllUsersByUsernamePrefix(prefix))

    @GetMapping
    @Operation(
        summary = "Returns all users",
        description = "Returns all users as list. Returns empty list if none found"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully returns list of users",
                content = [Content(
                    mediaType = "application/json",
                    array = io.swagger.v3.oas.annotations.media.ArraySchema(
                        schema = Schema(implementation = UserDtoResponse::class)
                    )
                )]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    fun getAllUsers() = ResponseEntity.ok(userService.findAll())

    @PatchMapping("/{id}")
    @Operation(summary = "Updating existing user", description = "Updates user and returns updated user")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Updated successfully. Returns updated user",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = UserDtoResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Validation error",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    fun updateUserById(
        @PathVariable id: UUID,
        @RequestBody @Valid request: UserDtoUpdateRequest
    ) = ResponseEntity.ok(userService.updateById(id, request))

    @PatchMapping("/me")
    @Operation(summary = "Updating current user", description = "Updates current user and returns it")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Updated successfully. Returns updated user",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = UserDtoResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Validation error",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    fun updateMe(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @RequestBody @Valid request: UserDtoUpdateMeRequest
    ): ResponseEntity<UserDtoResponse> {
        val id = userDetails.getId()
        return ResponseEntity.ok(userService.updateMeById(id!!, request))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleting existing user", description = "Deletes user. Returns nothing")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "Deleted successfully. No content"
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    fun deleteUserById(@PathVariable id: UUID): ResponseEntity<Void> {
        userService.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/me")
    @Operation(summary = "Deleting current user", description = "Deletes user. Returns nothing")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "Deleted successfully. No content"
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    fun deleteMe(@AuthenticationPrincipal userDetails: CustomUserDetails): ResponseEntity<Void> {
        val id = userDetails.getId()
        userService.deleteById(id!!)
        return ResponseEntity.noContent().build()
    }
}
