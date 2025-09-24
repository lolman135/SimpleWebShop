package project.api.controller.business

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import project.api.dto.request.business.CategoryDtoRequest
import project.api.dto.response.business.CategoryDtoResponse
import project.api.dto.response.error.ErrorResponse
import project.api.service.business.category.CategoryService
import java.net.URI
import java.util.UUID

@RestController
@RequestMapping("/api/v1/categories")
class CategoryController(private val categoryService: CategoryService) {

    @PostMapping
    @Operation(summary = "Creating new category", description = "Creates new category if not exists yet")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Created successfully. Returns created category",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = CategoryDtoResponse::class)
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
                responseCode = "409",
                description = "Conflict",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    fun addCategory(@RequestBody @Valid request: CategoryDtoRequest): ResponseEntity<CategoryDtoResponse> {
        val response = categoryService.save(request)
        val location = URI.create("/categories/${response.id}")
        return ResponseEntity.created(location).body(response)
    }

    @GetMapping
    @Operation(
        summary = "Returns all categories",
        description = "Returns all categories as list. If nothing to return, returns empty list"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully returns list of categories",
                content = [Content(
                    mediaType = "application/json",
                    array = io.swagger.v3.oas.annotations.media.ArraySchema(
                        schema = Schema(implementation = CategoryDtoResponse::class)
                    )
                )]
            )
        ]
    )
    fun getAllCategories() = ResponseEntity.ok(categoryService.findAll())

    @GetMapping("/{id}")
    @Operation(summary = "Returns category", description = "Returns category by id if exists")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully returns category",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = CategoryDtoResponse::class)
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
    fun getCategoryById(@PathVariable id: UUID) = ResponseEntity.ok(categoryService.findById(id))

    @PutMapping("/{id}")
    @Operation(summary = "Updating existed category", description = "Updates category and returns it in response")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Updated successfully. Returns updated category",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = CategoryDtoResponse::class)
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
    fun updateCategoryById(@PathVariable id: UUID, @RequestBody @Valid request: CategoryDtoRequest) =
        ResponseEntity.ok(categoryService.updateById(id, request))

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleting existed category", description = "Deletes category. Returns nothing")
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
            )
        ]
    )
    fun deleteCategoryById(@PathVariable id: UUID): ResponseEntity<Void> {
        categoryService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
