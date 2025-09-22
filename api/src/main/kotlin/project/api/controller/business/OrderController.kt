package project.api.controller.business

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
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
import project.api.dto.request.business.OrderDtoRequest
import project.api.dto.response.business.OrderDtoResponse
import project.api.entity.Order
import project.api.security.CustomUserDetails
import project.api.service.business.order.OrderService
import project.api.service.business.user.UserService
import java.net.URI
import java.util.UUID

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(private val orderService: OrderService, private val userService: UserService) {

    @PostMapping
    @Operation(summary = "Creating new order", description = "Creates new order if not exists yet")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Created successfully. Returns created order"),
            ApiResponse(responseCode = "400", description = "Validation error"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "409", description = "Conflict")
        ]
    )
    fun createOrder(
        @RequestBody @Valid request: OrderDtoRequest,
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ) : ResponseEntity<OrderDtoResponse> {
        val response = orderService.save(request, userDetails.user)
        val location = URI.create("/orders/${response.id}")
        return ResponseEntity.created(location).body(response)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Returns order", description = "Returns order by id if exists")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returns order"),
            ApiResponse(responseCode = "404", description = "Not found"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden")
        ]
    )
    fun getOrderById(@PathVariable id: UUID) = ResponseEntity.ok(orderService.findById(id))

    @GetMapping
    @Operation(
        summary = "Returns all orders",
        description = "Returns all orders as list. If nothing to return, than returns empty list")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returns list of orders"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden"),
        ]
    )
    fun getAllOrders() = ResponseEntity.ok(orderService.findAll())

    @GetMapping("/my")
    @Operation(
        summary = "Returns all orders for current user",
        description = "Returns all for current user orders as list. If nothing to return, than returns empty list")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returns list of orders"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
        ]
    )
    fun getAllProductsForUser(@AuthenticationPrincipal userDetails: CustomUserDetails) =
        ResponseEntity.ok(orderService.findAllForUser(userService.findRawUserById(userDetails.getId()!!)))

    @PutMapping("/{id}")
    @Operation(summary = "Updating existed order", description = "Updates order and returns it in response")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Updated successfully. Returns updated order"),
            ApiResponse(responseCode = "400", description = "Validation error"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Not found")
        ]
    )
    fun updateOrderById(
        @PathVariable id: UUID,
        @RequestBody @Valid request: OrderDtoRequest,
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ) : ResponseEntity<OrderDtoResponse> {
        val response = orderService.updateById(id, request, userDetails.user)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleting existed order", description = "Deletes order. Returns nothing")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Deleted successfully. No contend"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Not found")
        ]
    )
    fun deleteById(@PathVariable id: UUID): ResponseEntity<Void> {
        orderService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}