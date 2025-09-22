package project.api.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "roles")
class Role(
    @get:Id
    @get:Column(name = "id", nullable = false)
    @get:GeneratedValue()
    var id: UUID? = null,

    @get:Column(name = "name", nullable = false)
    var name: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Role) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Role(id=$id, name='$name')"
    }
}
