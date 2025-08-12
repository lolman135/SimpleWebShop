package project.api.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "orders")
class Order(
    @get:Id
    @get:GeneratedValue(strategy = GenerationType.UUID)
    @get:Column(name = "id")
    open var id: UUID? = null,

    @get:Column(name = "created_at", nullable = false)
    open var createdAt: LocalDateTime = LocalDateTime.now(),

    //the price is indicated in cents
    @get:Column(name = "total_cost", nullable = false)
    open var totalCost: Int = 0,

    @get:ManyToOne
    @get:JoinColumn(name = "user_id", nullable = false)
    open var user: User? = null,

    @get:ManyToMany
    @get:JoinTable(
        name = "orders_products",
        joinColumns = [JoinColumn(name = "order_id")],
        inverseJoinColumns = [JoinColumn(name = "product_id")]
    )
    open var products: MutableSet<Product> = mutableSetOf()

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Order) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Order(id=$id, createdAt=$createdAt, totalCost=$totalCost)"
    }
}