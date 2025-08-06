package project.api.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "products")
open class Product (
    @get:Id
    @get:GeneratedValue(strategy = GenerationType.UUID)
    @get:Column(name = "id")
    open var id: UUID?,

    @get:Column(name = "name")
    open var name: String,

    @get:Column(name = "price")
    open var price: Int,

    @get:Column(name = "image_url")
    open var imageUrl: String,

    @get:Column(name = "description")
    open var description: String
)