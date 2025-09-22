package project.api.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "categories")
class Category(
    @get:Id
    @get:GeneratedValue(strategy = GenerationType.UUID)
    @get:Column(name = "id")
    var id: UUID? = null,

    @get:Column(name = "name")
    var name: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Category) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Category(id=$id, name='$name')"
    }
}