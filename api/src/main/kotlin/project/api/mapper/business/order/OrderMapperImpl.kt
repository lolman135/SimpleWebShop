package project.api.mapper.business.order

import org.springframework.stereotype.Component
import project.api.dto.business.OrderDto
import project.api.entity.Order
import project.api.entity.User
import project.api.repository.product.ProductRepository

@Component
class OrderMapperImpl(val productRepository: ProductRepository) : OrderMapper {

    override fun toOrder(orderDto: OrderDto, user: User): Order {
        if(orderDto.items.isEmpty()){
            throw IllegalArgumentException("Wrong data provided!")
        }

        val productsWithAmount = orderDto.items.map {
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
}