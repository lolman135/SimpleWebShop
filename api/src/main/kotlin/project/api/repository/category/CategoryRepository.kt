package project.api.repository.category

import org.springframework.data.jpa.repository.JpaRepository
import project.api.entity.Category
import java.util.Optional
import java.util.UUID

interface CategoryRepository : JpaRepository<Category, UUID> {
    fun findCategoryByName(name: String): Optional<Category>
}