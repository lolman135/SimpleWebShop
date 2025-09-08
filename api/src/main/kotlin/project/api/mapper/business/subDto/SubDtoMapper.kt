package project.api.mapper.business.subDto

import project.api.dto.response.business.subDto.FeedbackSubDto
import project.api.dto.response.business.subDto.OrderSubDto
import project.api.dto.response.business.subDto.ProductSubDto
import project.api.dto.response.business.subDto.UserSubDto
import project.api.entity.Feedback
import project.api.entity.Order
import project.api.entity.Product
import project.api.entity.User

fun User.toSubDto() = this.toSubDtoInternal()
fun Order.toSubDto() = this.toSubDtoInternal()
fun Feedback.toSubDto() = this.toSubDtoInternal()
fun Product.toSubDto() = this.toSubDtoInternal()

//hidden implementation
private fun User.toSubDtoInternal() = UserSubDto(
    id = this.id ?: throw IllegalArgumentException("Id is not provided"),
    username = this.username,
    email = this.email
)

private fun Order.toSubDtoInternal() = OrderSubDto(
    id = this.id ?: throw IllegalArgumentException("Id is not provided"),
    totalCost = this.totalCost,
    productIds = this.products.map { it.id ?: throw IllegalArgumentException("Id is not provided") }
)

private fun Feedback.toSubDtoInternal() = FeedbackSubDto(
    id = this.id ?: throw IllegalArgumentException("Id is not provided"),
    rate = this.rate,
    review = this.review ?: ""
)

private fun Product.toSubDtoInternal() = ProductSubDto(
    id = this.id ?: throw IllegalArgumentException("Id is not provided"),
    name = this.name,
    price = this.price
)