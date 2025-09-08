package project.api.service.business.prodcut

import project.api.dto.request.business.CategoryDtoRequest
import project.api.dto.request.business.ProductDtoRequest
import project.api.entity.Product
import java.util.*

interface ProductService {
    fun save(dto: ProductDtoRequest): Product
    fun findAll(): List<Product>
    fun findById(id: UUID): Product
    fun updateById(id: UUID, dto: ProductDtoRequest): Product
    fun deleteById(id: UUID): Boolean
    fun findProductsByCategory(dto: CategoryDtoRequest): List<Product>
}