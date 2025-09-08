package project.api.mapper.business.product

import org.springframework.stereotype.Component
import project.api.dto.request.business.ProductDtoRequest
import project.api.dto.response.business.ProductDtoResponse
import project.api.entity.Product
import project.api.exception.EntityNotFoundException
import project.api.mapper.business.subDto.toSubDto
import project.api.repository.category.CategoryRepository
import project.api.repository.feedback.FeedbackRepository

@Component
class ProductMapperImpl(
    val feedbackRepository: FeedbackRepository,
    val categoryRepository: CategoryRepository
) : ProductMapper {

    override fun toProduct(request: ProductDtoRequest): Product {
        val category = categoryRepository.findById(request.categoryId)
            .orElseThrow { EntityNotFoundException("Category with id=${request.categoryId} not found") }

        val product = Product(
            name = request.name,
            description = request.description,
            price = request.price,
            imageUrl = request.imageUrl,
            category = category
        )

        product.feedbacks = request.feedbackIds.map {
            feedbackRepository.findById(it).orElseThrow {
                IllegalArgumentException("Invalid id provided!")
            }
        }.toMutableSet()

        return product
    }

    override fun toDto(product: Product) = ProductDtoResponse(
        id = product.id ?: throw IllegalArgumentException("Id is not provided"),
        name = product.name,
        description = product.description,
        price = product.price,
        imageUrl = product.imageUrl,
        category = product.category.name,
        feedbacks = product.feedbacks.map{it.toSubDto()}
    )
}