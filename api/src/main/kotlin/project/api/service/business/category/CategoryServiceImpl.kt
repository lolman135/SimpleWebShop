package project.api.service.business.category

import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service
import project.api.dto.request.business.CategoryDtoRequest
import project.api.dto.response.business.CategoryDtoResponse
import project.api.exception.EntityAlreadyExistsException
import project.api.exception.EntityNotFoundException
import project.api.mapper.business.category.CategoryMapper
import project.api.repository.category.CategoryRepository
import java.util.*

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val categoryMapper: CategoryMapper
): CategoryService {

    @Caching(
        evict = [
            CacheEvict(value = ["categoryList"], allEntries = true),
            CacheEvict(value = ["categories"], key = "#id"),
            CacheEvict(value = ["productList"], allEntries = true),
            CacheEvict(value = ["productsByCategory"], allEntries = true)
        ]
    )
    override fun deleteById(id: UUID): Boolean {
        categoryRepository.deleteById(id)
        return true
    }

    @CacheEvict(value = ["categoryList"], allEntries = true)
    override fun save(dto: CategoryDtoRequest): CategoryDtoResponse {
        if(categoryRepository.existsCategoryByName(dto.name ))
            throw EntityAlreadyExistsException("This category is already exists")

        val category = categoryMapper.toCategory(dto)
        val savedCategory = categoryRepository.save(category)
        return categoryMapper.toDto(savedCategory)
    }

    @Cacheable(value = ["categoryList"])
    override fun findAll(): List<CategoryDtoResponse> = categoryRepository.findAll().map { categoryMapper.toDto(it) }

    @Cacheable(value = ["categories"], key = "#id")
    override fun findById(id: UUID): CategoryDtoResponse {
        val category = categoryRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Entity with id=$id not found") }
        return categoryMapper.toDto(category)
    }

    @Caching(
        put = [CachePut(value = ["categories"], key = "#id")],
        evict = [
            CacheEvict(value = ["categoryList"], allEntries = true),
            CacheEvict(value = ["productList"], allEntries = true),
            CacheEvict(value = ["productsByCategory"], allEntries = true)
        ]
    )
    override fun updateById(id: UUID, dto: CategoryDtoRequest): CategoryDtoResponse {
        if (!categoryRepository.existsById(id))
            throw EntityNotFoundException("Category with id=$id not found")
        val category = categoryMapper.toCategory(dto)
        category.id = id
        val updatedCategory = categoryRepository.save(category)
        return categoryMapper.toDto(updatedCategory)
    }
}