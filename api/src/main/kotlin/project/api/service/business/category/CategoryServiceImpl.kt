package project.api.service.business.category

import org.springframework.stereotype.Service
import project.api.dto.request.business.CategoryDtoRequest
import project.api.dto.response.business.CategoryDtoResponse
import project.api.exception.EntityNotFoundException
import project.api.mapper.business.category.CategoryMapper
import project.api.repository.category.CategoryRepository
import java.util.*

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val categoryMapper: CategoryMapper
): CategoryService {

    override fun deleteById(id: UUID): Boolean {
        if (!categoryRepository.existsById(id))
            throw EntityNotFoundException("Category with id=$id not found")
        categoryRepository.deleteById(id)
        return true
    }

    override fun save(dto: CategoryDtoRequest): CategoryDtoResponse {
        val category = categoryMapper.toCategory(dto)
        val savedCategory = categoryRepository.save(category)
        return categoryMapper.toDto(savedCategory)
    }

    override fun findAll(): List<CategoryDtoResponse> = categoryRepository.findAll().map { categoryMapper.toDto(it) }

    override fun findById(id: UUID): CategoryDtoResponse {

        val category = categoryRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Entity with id=$id not found") }
        return categoryMapper.toDto(category)
    }

    override fun updateById(id: UUID, dto: CategoryDtoRequest): CategoryDtoResponse {
        if (!categoryRepository.existsById(id))
            throw EntityNotFoundException("Category with id=$id not found")
        val category = categoryMapper.toCategory(dto)
        category.id = id
        val updatedCategory = categoryRepository.save(category)
        return categoryMapper.toDto(updatedCategory)
    }
}