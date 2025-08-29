package project.api.mapper.business.product

import project.api.dto.business.ProductDto
import project.api.entity.Product

interface ProductMapper {

    fun toProduct(productDto: ProductDto): Product
}