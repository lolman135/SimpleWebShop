package project.api.controller.business

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import project.api.dto.business.OrderDto
import project.api.entity.Order
import project.api.security.CustomUserDetails
import project.api.service.business.order.OrderService
import java.util.UUID

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(private val orderService: OrderService, ) {

    @PostMapping
    fun createOrder(
        @RequestBody request: OrderDto,
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ) : ResponseEntity<Order> {
        val order = orderService.save(request, userDetails.user)
        return ResponseEntity.ok(order)
    }

    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: UUID) = ResponseEntity.ok(orderService.findById(id))

    @GetMapping
    fun getAllOrders() = ResponseEntity.ok(orderService.findAll())

    @PutMapping("/{id}")
    fun updateOrderById(
        @PathVariable id: UUID,
        @RequestBody request: OrderDto,
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ) : ResponseEntity<Order> {
        val order = orderService.updateById(id, request, userDetails.user)
        return ResponseEntity.ok(order)
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: UUID) = ResponseEntity.ok(orderService.deleteById(id))
}