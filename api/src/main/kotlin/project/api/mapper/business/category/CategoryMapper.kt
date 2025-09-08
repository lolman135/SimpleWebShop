package project.api.mapper.business.category

import project.api.dto.request.business.CategoryDtoRequest
import project.api.dto.response.business.subDto.CategoryDtoResponse
import project.api.entity.Category

interface CategoryMapper {
    fun toCategory(request: CategoryDtoRequest): Category
    fun toDto(category: Category): CategoryDtoResponse
}