package project.api.mapper.order

import org.springframework.stereotype.Component
import project.api.dto.OrderDto
import project.api.entity.Order
import project.api.entity.User
import project.api.repository.product.ProductRepository

@Component
class OrderMapperImpl(val productRepository: ProductRepository) : OrderMapper {

    override fun mapToOrder(orderDto: OrderDto, user: User): Order {

        val order = Order()

        order.user = user

        if(orderDto.items.isEmpty()){
            throw IllegalArgumentException("Wrong data provided!")
        }

        val productsWithAmount = orderDto.items.map {
            val product = productRepository.findById(it.productId).orElseThrow {
                IllegalArgumentException("Wrong Id!")
            }
            product to it.productsAmount
        }

        order.products = productsWithAmount.map { it.first }.toMutableSet()

        order.totalCost = productsWithAmount.sumOf { (product, amount) ->
            product.price * amount
        }

        return order
    }
}