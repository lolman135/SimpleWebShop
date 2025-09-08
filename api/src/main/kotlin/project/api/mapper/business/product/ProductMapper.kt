package project.api.mapper.business.product

import project.api.dto.request.business.ProductDtoRequest
import project.api.dto.response.business.ProductDtoResponse
import project.api.entity.Product

interface ProductMapper {
    fun toProduct(request: ProductDtoRequest): Product
    fun toDto(product: Product): ProductDtoResponse
}