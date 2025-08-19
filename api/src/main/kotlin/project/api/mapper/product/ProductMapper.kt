package project.api.mapper.product

import project.api.dto.ProductDto
import project.api.entity.Product

interface ProductMapper {

    fun toProduct(productDto: ProductDto): Product
}