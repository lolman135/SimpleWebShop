package project.api.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "products")
class Product(
    @get:Id
    @get:GeneratedValue(strategy = GenerationType.UUID)
    @get:Column(name = "id")
    open var id: UUID? = null,

    @get:Column(name = "name")
    open var name: String,

    //the price is indicated in cents
    @get:Column(name = "price")
    open var price: Int,

    @get:Column(name = "image_url")
    open var imageUrl: String,

    @get:Column(name = "description")
    open var description: String,

    @get:OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    open var feedbacks: MutableSet<Feedback> = mutableSetOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Product) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Product(description='$description', id=$id, name='$name', price=$price, imageUrl='$imageUrl')"
    }

}