package project.api.repository.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import project.api.entity.Product
import java.util.*

@Repository
interface ProductRepository : JpaRepository<Product, UUID> {
    fun id(id: UUID): MutableList<Product>
}