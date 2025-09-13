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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import project.api.dto.request.business.CategoryDtoRequest
import project.api.dto.request.business.ProductDtoRequest
import project.api.service.business.prodcut.ProductService
import java.util.UUID

@RestController
@RequestMapping("/api/v1/products")
class ProductController(private val productService: ProductService) {

    @PostMapping
    fun addProduct(@RequestBody @Valid request: ProductDtoRequest) = ResponseEntity.ok(productService.save(request))

    @GetMapping
    fun getAllProducts() = ResponseEntity.ok(productService.findAll())

    @GetMapping("/by-category")
    fun getProductsByCategory(@RequestBody @Valid request: CategoryDtoRequest) =
        ResponseEntity.ok(productService.findProductsByCategory(request))

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: UUID) = ResponseEntity.ok(productService.findById(id))

    @PutMapping("/{id}")
    fun updateProductById(@PathVariable id: UUID, @RequestBody @Valid request: ProductDtoRequest) =
        ResponseEntity.ok(productService.updateById(id, request))

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: UUID): ResponseEntity<Void>{
        productService.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/name")
    fun getProductsByNamePrefix(@RequestParam prefix: String) =
        ResponseEntity.ok(productService.findProductByNamePrefix(prefix))
}