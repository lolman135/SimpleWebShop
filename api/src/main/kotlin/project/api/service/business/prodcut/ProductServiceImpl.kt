package project.api.service.business.prodcut

import jakarta.transaction.Transactional
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service
import project.api.dto.request.business.ProductDtoRequest
import project.api.dto.response.business.ProductDtoResponse
import project.api.exception.EntityAlreadyExistsException
import project.api.exception.EntityNotFoundException
import project.api.mapper.business.category.CategoryMapper
import project.api.mapper.business.product.ProductMapper
import project.api.repository.category.CategoryRepository
import project.api.repository.product.ProductRepository
import java.util.*

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val productMapper: ProductMapper,
    private val categoryRepository: CategoryRepository
) : ProductService {

    @Caching(
        evict = [
            CacheEvict(value = ["productList"], allEntries = true ),
            CacheEvict(value = ["products"], key = "#id"),
            CacheEvict(value = ["productsByCategory"], allEntries = true),
            CacheEvict(value = ["orderList"], allEntries = true),
            CacheEvict(value = ["orderForUser"], allEntries = true),
            CacheEvict(value = ["orders"], allEntries = true),
            CacheEvict(value = ["feedbackList"], allEntries = true),
            CacheEvict(value = ["feedbackForUser"], allEntries = true),
            CacheEvict(value = ["feedbacks"], allEntries = true)
        ]
    )
    override fun deleteById(id: UUID): Boolean {
        if (!productRepository.existsById(id))
            throw EntityNotFoundException("Product with id=$id not found")
        productRepository.deleteById(id)
        return true
    }

    @Transactional
    @Caching(
        evict = [
            CacheEvict(value = ["productsByCategory"], key = "#result.category.toLowerCase()"),
            CacheEvict(value = ["productList"], allEntries = true )
        ]
    )
    override fun save(dto: ProductDtoRequest): ProductDtoResponse {
        if (productRepository.existsProductByName(dto.name))
            throw EntityAlreadyExistsException("This product is already exists")

        val product = productMapper.toProduct(dto)
        val savedProduct = productRepository.save(product)
        return productMapper.toDto(savedProduct)
    }

    @Transactional
    @Cacheable(value = ["productList"])
    override fun findAll(): List<ProductDtoResponse> = productRepository.findAll().map { productMapper.toDto(it) }

    @Transactional
    @Cacheable(value = ["products"], key = "#id")
    override fun findById(id: UUID): ProductDtoResponse {
        val product = productRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Product with id=$id not found") }
        return productMapper.toDto(product)
    }

    @Transactional
    @Caching(
        put = [CachePut(value = ["products"], key = "#id")],
        evict = [
            CacheEvict(value = ["productList"], allEntries = true  ),
            CacheEvict(value = ["productsByCategory"], key = "#result.category.toLowerCase()"),
            CacheEvict(value = ["productsByCategory"], allEntries = true),
            CacheEvict(value = ["orderList"], allEntries = true),
            CacheEvict(value = ["orderForUser"], allEntries = true),
            CacheEvict(value = ["orders"], allEntries = true),
            CacheEvict(value = ["feedbackList"], allEntries = true),
            CacheEvict(value = ["feedbackForUser"], allEntries = true),
            CacheEvict(value = ["feedbacks"], allEntries = true)
        ]
    )
    override fun updateById(id: UUID, dto: ProductDtoRequest): ProductDtoResponse {
        if (!productRepository.existsById(id))
            throw EntityNotFoundException("Product with id=$id not found")

        val product = productMapper.toProduct(dto)
        product.id = id
        val updatedProduct = productRepository.save(product)
        return productMapper.toDto(updatedProduct)
    }

    @Transactional
    @Cacheable(value = ["productsByCategory"], key = "#categoryName.toLowerCase()")
    override fun findProductsByCategory(categoryName: String): List<ProductDtoResponse> {
        val category = categoryRepository.findCategoryByName(categoryName)
            .orElseThrow {EntityNotFoundException("Category with name $categoryName not found")}
        return productRepository.findProductsByCategory(category).map { productMapper.toDto(it) }
    }

    override fun findProductByNamePrefix(prefix: String) =
        productRepository.findProductByNamePrefix(prefix).map { productMapper.toDto(it) }
}