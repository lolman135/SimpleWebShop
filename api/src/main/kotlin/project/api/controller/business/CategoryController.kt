package project.api.controller.business

import io.swagger.v3.oas.annotations.Operation
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
import project.api.service.business.category.CategoryService
import java.net.URI
import java.util.UUID

@RestController
@RequestMapping("/api/v1/categories")
class CategoryController(private val categoryService: CategoryService) {

    @PostMapping
    @Operation(summary = "Adding new category", description = "Creates new category and returns 201 if created")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Success authentication, returns JWT"),
            ApiResponse(responseCode = "400", description = "Validation error"),
            ApiResponse(responseCode = "401", description = "Invalid username or password")
        ]
    )
    fun addCategory(@RequestBody request: @Valid CategoryDtoRequest): ResponseEntity<CategoryDtoResponse> {
        val response = categoryService.save(request)
        val location = URI.create("/categories/${response.id}")
        return ResponseEntity.created(location).body(response)
    }

    @GetMapping
    fun getAllCategories() = ResponseEntity.ok(categoryService.findAll())

    @GetMapping("/{id}")
    fun getCategoryById(@PathVariable id:UUID) = ResponseEntity.ok(categoryService.findById(id))

    @PutMapping("/{id}")
    fun updateCategoryById(@PathVariable id:UUID, @RequestBody @Valid request: CategoryDtoRequest) =
        ResponseEntity.ok(categoryService.updateById(id, request))

    @DeleteMapping("/{id}")
    fun deleteCategoryById(@PathVariable id:UUID): ResponseEntity<Void> {
        categoryService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}