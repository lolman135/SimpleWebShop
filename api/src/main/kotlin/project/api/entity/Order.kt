package project.api.entity

import jakarta.persistence.*
import java.time.LocalTime
import java.util.*

@Entity
@Table(name = "orders")
class Order(
    @get:Id
    @get:GeneratedValue(strategy = GenerationType.UUID)
    @get:Column(name = "id")
    open var id: UUID?,

    @get:Column(name = "created_at")
    open var createdAt: LocalTime,

    @get:Column(name = "total_cost")
    open var totalCost: Int,

    @get:ManyToOne
    @get:JoinColumn(name = "user_id", nullable = false)
    open var user: User,

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