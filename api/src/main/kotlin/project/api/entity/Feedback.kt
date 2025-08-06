package project.api.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "feedbacks")
class Feedback(
    @get:Id
    @get:GeneratedValue(strategy = GenerationType.UUID)
    @get:Column(name = "id")
    open var id: UUID?,

    @get:Column(name = "message")
    open var message: String?,

    @get:Column(name = "rate")
    open var rate: Int,

    @get:ManyToOne
    @get:JoinColumn(name="user_id", nullable = false)
    open var user: User,

    @get:ManyToOne
    @get:JoinColumn(name="product_id", nullable = false)
    open var product: Product
){
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
        return "Feedback(id=$id, message=$message, rate=$rate)"
    }
}

