package project.api.service.business.category

import project.api.dto.business.CategoryDto
import project.api.entity.Category
import java.util.*

interface CategoryService {
    fun save(dto: CategoryDto): Category
    fun findAll(): List<Category>
    fun findById(id: UUID): Category
    fun updateById(id: UUID, dto: CategoryDto): Category
    fun deleteById(id: UUID): Boolean
}