package project.api.mapper.business.category

import org.springframework.stereotype.Component
import project.api.dto.request.business.CategoryDtoRequest
import project.api.dto.response.business.CategoryDtoResponse
import project.api.entity.Category

@Component
class CategoryMapperImpl : CategoryMapper {
    override fun toCategory(request: CategoryDtoRequest) = Category(name = request.name)
    override fun toDto(category: Category) = CategoryDtoResponse(
        id = category.id ?: throw IllegalArgumentException("Id is not provided"),
        name = category.name
    )
}