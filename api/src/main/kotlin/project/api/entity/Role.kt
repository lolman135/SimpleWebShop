package project.api.entity

import jakarta.persistence.*

@Entity
@Table(name = "roles")
class Role (
    @get:Id
    @get:Column(name = "id", nullable = false)
    @get:GeneratedValue()
    open var id: String?,

    @get:Column(name = "name", nullable = false, length = 20)
    open var name: String,
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
