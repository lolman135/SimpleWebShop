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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import project.api.dto.request.business.CategoryDtoRequest
import project.api.dto.request.business.ProductDtoRequest
import project.api.dto.response.business.ProductDtoResponse
import project.api.service.business.prodcut.ProductService
import java.net.URI
import java.util.UUID

@RestController
@RequestMapping("/api/v1/products")
class ProductController(private val productService: ProductService) {

    @PostMapping
    @Operation(summary = "Creating new product", description = "Creates new product if not exists yet")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Created successfully. Returns created order"),
            ApiResponse(responseCode = "400", description = "Validation error"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden"),
            ApiResponse(responseCode = "409", description = "Conflict")
        ]
    )
    fun addProduct(@RequestBody @Valid request: ProductDtoRequest): ResponseEntity<ProductDtoResponse>{
        val response = productService.save(request)
        val location = URI.create("/products/${response.id}")
        return ResponseEntity.created(location).body(response)
    }

    @GetMapping
    @Operation(
        summary = "Returns all products",
        description = "Returns all products as list. If nothing to return, than returns empty list")
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "Successfully returns list of products")]
    )
    fun getAllProducts() = ResponseEntity.ok(productService.findAll())

    @GetMapping("/category")
    @Operation(
        summary = "Returns all products of this category",
        description = "Returns all products for current category as list. If nothing to return, than returns empty list")
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "Successfully returns list of products")]
    )
    fun getProductsByCategory(@RequestParam name: String) =
        ResponseEntity.ok(productService.findProductsByCategory(name))

    @GetMapping("/{id}")
    @Operation(summary = "Returns product", description = "Returns product by id if exists")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returns product"),
            ApiResponse(responseCode = "404", description = "Not found")
        ]
    )
    fun getProductById(@PathVariable id: UUID) = ResponseEntity.ok(productService.findById(id))

    @PutMapping("/{id}")
    @Operation(summary = "Updating existed product", description = "Updates product and returns it in response")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Updated successfully. Returns updated product"),
            ApiResponse(responseCode = "400", description = "Validation error"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden"),
            ApiResponse(responseCode = "404", description = "Not found")
        ]
    )
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