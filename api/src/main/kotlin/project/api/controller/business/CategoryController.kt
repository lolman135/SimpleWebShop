package project.api.controller.business

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
import project.api.service.business.category.CategoryService
import java.util.UUID

@RestController
@RequestMapping("/api/v1/categories")
class CategoryController(private val categoryService: CategoryService) {

    @PostMapping
    fun addCategory(@RequestBody request: @Valid CategoryDtoRequest) = ResponseEntity.ok(categoryService.save(request))

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