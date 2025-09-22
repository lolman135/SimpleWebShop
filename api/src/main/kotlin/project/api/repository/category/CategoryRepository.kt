package project.api.repository.category

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import project.api.entity.Category
import java.util.Optional
import java.util.UUID

@Repository
interface CategoryRepository : JpaRepository<Category, UUID> {
    fun findCategoryByName(name: String): Optional<Category>
    fun existsCategoryByName(name: String): Boolean
}