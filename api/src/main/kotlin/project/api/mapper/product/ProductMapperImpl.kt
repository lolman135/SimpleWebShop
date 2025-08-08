package project.api.mapper.product

import org.springframework.stereotype.Component
import project.api.dto.ProductDto
import project.api.entity.Product
import project.api.repository.feedback.FeedbackRepository

@Component
class ProductMapperImpl(val feedbackRepository: FeedbackRepository) : ProductMapper {


    override fun mapToProduct(productDto: ProductDto): Product {
        val product = Product(
            name = productDto.name,
            description = productDto.description,
            price = productDto.price,
            imageUrl = productDto.imageUrl
        )

        product.feedbacks = productDto.feedbackIds.map {
            feedbackRepository.findById(it).orElseThrow {
                IllegalArgumentException("Invalid id provided!")
            }
        }.toMutableSet()

        return product
    }
}