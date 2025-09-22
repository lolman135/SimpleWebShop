package project.api.repository.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import project.api.entity.Category
import project.api.entity.Product
import java.util.*

@Repository
interface ProductRepository : JpaRepository<Product, UUID>{
    fun findProductsByCategory(category: Category): List<Product>
    fun existsProductByName(name: String): Boolean

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE CONCAT(LOWER(:prefix), '%') ")
    fun findProductByNamePrefix(@Param("prefix") prefix: String): List<Product>
}