package project.api.mapper.business.product

import org.springframework.stereotype.Component
import project.api.dto.business.ProductDto
import project.api.entity.Product
import project.api.exception.EntityNotFoundException
import project.api.repository.category.CategoryRepository
import project.api.repository.feedback.FeedbackRepository

@Component
class ProductMapperImpl(
    val feedbackRepository: FeedbackRepository,
    val categoryRepository: CategoryRepository
) : ProductMapper {

    override fun toProduct(productDto: ProductDto): Product {
        val category = categoryRepository.findById(productDto.categoryId)
            .orElseThrow { EntityNotFoundException("Category with id=${productDto.categoryId} not found") }

        val product = Product(
            name = productDto.name,
            description = productDto.description,
            price = productDto.price,
            imageUrl = productDto.imageUrl,
            category = category
        )

        product.feedbacks = productDto.feedbackIds.map {
            feedbackRepository.findById(it).orElseThrow {
                IllegalArgumentException("Invalid id provided!")
            }
        }.toMutableSet()

        return product
    }
}