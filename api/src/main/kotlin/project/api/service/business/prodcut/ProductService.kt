package project.api.service.business.prodcut

import project.api.dto.request.business.ProductDtoRequest
import project.api.dto.response.business.ProductDtoResponse
import java.util.*

interface ProductService {
    fun save(dto: ProductDtoRequest): ProductDtoResponse
    fun findAll(): List<ProductDtoResponse>
    fun findById(id: UUID): ProductDtoResponse
    fun updateById(id: UUID, dto: ProductDtoRequest): ProductDtoResponse
    fun deleteById(id: UUID): Boolean
    fun findProductsByCategory(categoryName: String): List<ProductDtoResponse>
    fun findProductByNamePrefix(prefix: String): List<ProductDtoResponse>
}