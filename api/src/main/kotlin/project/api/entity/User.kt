package project.api.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "users")
class User(
    @get:Id
    @get:GeneratedValue(strategy = GenerationType.UUID)
    @get:Column(name = "id")
    open var id: UUID?,

    @get:Column(name = "username", nullable = false)
    open var username: String,

    @get:Column(name = "email", nullable = false)
    open var email: String,

    @get:Column(name = "password", nullable = false)
    open var password: String,

    @get:OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    open var orders: MutableSet<Order> = mutableSetOf(),

    @get:ManyToMany
    @get:JoinTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    open var roles: MutableSet<Role> = mutableSetOf(),

    @get:OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    open var feedbacks: MutableSet<Feedback> = mutableSetOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "User(email='$email', id=$id, username='$username')"
    }
}