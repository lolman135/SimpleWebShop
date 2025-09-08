package project.api.service.business.prodcut

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import project.api.dto.business.CategoryDto
import project.api.dto.business.ProductDto
import project.api.entity.Product
import project.api.exception.EntityNotFoundException
import project.api.mapper.business.category.CategoryMapper
import project.api.mapper.business.product.ProductMapper
import project.api.repository.product.ProductRepository
import java.util.*

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val productMapper: ProductMapper,
    private val categoryMapper: CategoryMapper
) : ProductService {

    override fun deleteById(id: UUID): Boolean {
        if (!productRepository.existsById(id))
            throw EntityNotFoundException("Product with id=$id not found")
        productRepository.deleteById(id)
        return true
    }

    @Transactional
    override fun save(dto: ProductDto): Product {
        val product = productMapper.toProduct(dto)
        return productRepository.save(product)
    }

    @Transactional
    override fun findAll(): List<Product> = productRepository.findAll()

    @Transactional
    override fun findById(id: UUID): Product = productRepository.findById(id).orElseThrow{
        EntityNotFoundException("Product with id=$id not found")
    }

    @Transactional
    override fun updateById(id: UUID, dto: ProductDto): Product {
        if (!productRepository.existsById(id))
            throw EntityNotFoundException("Product with id=$id not found")

        val product = productMapper.toProduct(dto)
        product.id = id
        return productRepository.save(product)
    }

    @Transactional
    override fun findProductsByCategory(dto: CategoryDto): List<Product> {
        val category = categoryMapper.toCategory(dto)
        return productRepository.findProductsByCategory(category)
    }
}