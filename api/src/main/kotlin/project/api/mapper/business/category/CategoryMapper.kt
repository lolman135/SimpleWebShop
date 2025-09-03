package project.api.mapper.business.category

import project.api.dto.business.CategoryDto
import project.api.entity.Category

interface CategoryMapper {
    fun toCategory(dto: CategoryDto): Category
}