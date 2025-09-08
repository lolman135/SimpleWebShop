package project.api.service.business.category

import project.api.dto.request.business.CategoryDtoRequest
import project.api.entity.Category
import java.util.*

interface CategoryService {
    fun save(dto: CategoryDtoRequest): Category
    fun findAll(): List<Category>
    fun findById(id: UUID): Category
    fun updateById(id: UUID, dto: CategoryDtoRequest): Category
    fun deleteById(id: UUID): Boolean
}