package project.api.service.business.prodcut

import project.api.dto.ProductDto
import project.api.entity.Product
import java.util.*

interface ProductService {
    fun save(dto: ProductDto): Product
    fun findAll(): List<Product>
    fun findById(): Product
    fun updateById(id: UUID, dto: ProductDto): Product
    fun deleteById(id: UUID): Boolean
}