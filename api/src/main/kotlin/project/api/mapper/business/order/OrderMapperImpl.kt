package project.api.mapper.business.order

import org.springframework.stereotype.Component
import project.api.dto.request.business.OrderDtoRequest
import project.api.dto.response.business.OrderDtoResponse
import project.api.entity.Order
import project.api.entity.User
import project.api.mapper.business.subDto.toSubDto
import project.api.repository.product.ProductRepository

@Component
class OrderMapperImpl(val productRepository: ProductRepository) : OrderMapper {

    override fun toOrder(request: OrderDtoRequest, user: User): Order {
        if(request.items.isEmpty()){
            throw IllegalArgumentException("Wrong data provided!")
        }

        val productsWithAmount = request.items.map {
            val product = productRepository.findById(it.productId).orElseThrow {
                IllegalArgumentException("Wrong Id!")
            }
            product to it.productsAmount
        }

        val totalCost = productsWithAmount.sumOf { (product, amount) ->
            product.price * amount
        }

        val order = Order(user = user, totalCost = totalCost)

        order.products = productsWithAmount.map { it.first }.toMutableSet()

        return order
    }

    override fun toDto(order: Order) = OrderDtoResponse(
        id = order.id ?: throw IllegalArgumentException("Id is not provided"),
        createdAt = order.createdAt,
        totalCost = order.totalCost,
        user = order.user.toSubDto(),
        products = order.products.map { it.toSubDto() }
    )
}