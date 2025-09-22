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
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import project.api.dto.response.error.ErrorResponse
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
            ApiResponse(
                responseCode = "201",
                description = "Created successfully. Returns created product",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ProductDtoResponse::class)
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
    fun addProduct(@RequestBody @Valid request: ProductDtoRequest): ResponseEntity<ProductDtoResponse> {
        val response = productService.save(request)
        val location = URI.create("/products/${response.id}")
        return ResponseEntity.created(location).body(response)
    }

    @GetMapping
    @Operation(
        summary = "Returns all products",
        description = "Returns all products as list. If nothing to return, returns empty list"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully returns list of products",
                content = [Content(
                    mediaType = "application/json",
                    array = io.swagger.v3.oas.annotations.media.ArraySchema(
                        schema = Schema(implementation = ProductDtoResponse::class)
                    )
                )]
            )
        ]
    )
    fun getAllProducts() = ResponseEntity.ok(productService.findAll())

    @GetMapping("/category")
    @Operation(
        summary = "Returns all products of this category",
        description = "Returns all products for current category as list"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully returns list of products",
                content = [Content(
                    mediaType = "application/json",
                    array = io.swagger.v3.oas.annotations.media.ArraySchema(
                        schema = Schema(implementation = ProductDtoResponse::class)
                    )
                )]
            )
        ]
    )
    fun getProductsByCategory(@RequestParam name: String) =
        ResponseEntity.ok(productService.findProductsByCategory(name))

    @GetMapping("/{id}")
    @Operation(summary = "Returns product", description = "Returns product by id if exists")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully returns product",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ProductDtoResponse::class)
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
    fun getProductById(@PathVariable id: UUID) = ResponseEntity.ok(productService.findById(id))

    @PutMapping("/{id}")
    @Operation(summary = "Updating existed product", description = "Updates product and returns it in response")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Updated successfully. Returns updated product",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ProductDtoResponse::class)
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
    fun updateProductById(@PathVariable id: UUID, @RequestBody @Valid request: ProductDtoRequest) =
        ResponseEntity.ok(productService.updateById(id, request))

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleting existed product", description = "Deletes product. Returns nothing")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Deleted successfully. No content"),
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
    fun deleteById(@PathVariable id: UUID): ResponseEntity<Void> {
        productService.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/name")
    @Operation(
        summary = "Returns products by name prefix",
        description = "Returns all products whose names start with prefix"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully returns list of products",
                content = [Content(
                    mediaType = "application/json",
                    array = io.swagger.v3.oas.annotations.media.ArraySchema(
                        schema = Schema(implementation = ProductDtoResponse::class)
                    )
                )]
            )
        ]
    )
    fun getProductsByNamePrefix(@RequestParam prefix: String) =
        ResponseEntity.ok(productService.findProductByNamePrefix(prefix))
}
