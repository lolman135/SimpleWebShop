package project.api.entity

import jakarta.persistence.*

@Entity
@Table(name = "roles")
open class Role (
    @get:Id
    @get:Column(name = "id", nullable = false)
    @get:GeneratedValue()
    open var id: String?,

    @get:Column(name = "name", nullable = false, length = 20)
    open var name: String,
)