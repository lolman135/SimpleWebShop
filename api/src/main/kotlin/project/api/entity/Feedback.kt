package project.api.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "feedbacks")
class Feedback(
    @get:Id
    @get:GeneratedValue(strategy = GenerationType.UUID)
    @get:Column(name = "id")
    var id: UUID? = null,

    @get:Column(name = "review")
    var review: String? = null,

    @get:Column(name = "rate")
    var rate: Int,

    @get:ManyToOne
    @get:JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @get:ManyToOne
    @get:JoinColumn(name = "product_id", nullable = false)
    var product: Product
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Feedback) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Feedback(id=$id, review=$review, rate=$rate)"
    }
}

