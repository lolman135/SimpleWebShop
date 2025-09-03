package project.api.mapper.business.category

import org.springframework.stereotype.Component
import project.api.dto.business.CategoryDto
import project.api.entity.Category

@Component
class CategoryMapperImpl : CategoryMapper {
    override fun toCategory(dto: CategoryDto) = Category(name = dto .name)
}