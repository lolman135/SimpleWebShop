package project.api.service.business.category

import project.api.dto.request.business.CategoryDtoRequest
import project.api.dto.response.business.CategoryDtoResponse
import java.util.*

interface CategoryService {
    fun save(dto: CategoryDtoRequest): CategoryDtoResponse
    fun findAll(): List<CategoryDtoResponse>
    fun findById(id: UUID): CategoryDtoResponse
    fun updateById(id: UUID, dto: CategoryDtoRequest): CategoryDtoResponse
    fun deleteById(id: UUID): Boolean
}