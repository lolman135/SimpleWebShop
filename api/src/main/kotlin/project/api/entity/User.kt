package project.api.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "users")
open class User(

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
    open var orders: MutableSet<Order> = mutableSetOf()
)