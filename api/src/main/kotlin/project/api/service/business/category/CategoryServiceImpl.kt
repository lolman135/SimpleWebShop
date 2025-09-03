package project.api.service.business.category

import org.springframework.stereotype.Service
import project.api.dto.business.CategoryDto
import project.api.entity.Category
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

    override fun save(dto: CategoryDto): Category {
        val category = categoryMapper.toCategory(dto)
        return categoryRepository.save(category)
    }

    override fun findAll(): List<Category> = categoryRepository.findAll()

    override fun findById(id: UUID): Category = categoryRepository.findById(id)
        .orElseThrow{ EntityNotFoundException("Entity with id=$id not found") }

    override fun updateById(id: UUID, dto: CategoryDto): Category {
        if (!categoryRepository.existsById(id))
            throw EntityNotFoundException("Category with id=$id not found")
        val category = categoryMapper.toCategory(dto)
        category.id = id
        return categoryRepository.save(category)
    }
}