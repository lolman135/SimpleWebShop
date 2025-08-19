package project.api.service.business.prodcut

import org.springframework.stereotype.Service
import project.api.dto.ProductDto
import project.api.entity.Product
import project.api.exception.EntityNotFoundException
import project.api.mapper.product.ProductMapper
import project.api.repository.product.ProductRepository
import java.util.*

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val productMapper: ProductMapper
) : ProductService {

    override fun deleteById(id: UUID): Boolean {
        if (!productRepository.existsById(id))
            throw EntityNotFoundException("Product with id=$id not found")

        productRepository.deleteById(id)
        return true
    }

    override fun save(dto: ProductDto): Product {
        val product = productMapper.toProduct(dto)
        return productRepository.save(product)
    }

    override fun findAll(): List<Product> = productRepository.findAll()

    override fun findById(id: UUID): Product = productRepository.findById(id).orElseThrow{
        EntityNotFoundException("Product with id=$id not found")
    }

    override fun updateById(id: UUID, dto: ProductDto): Product {
        if (!productRepository.existsById(id))
            throw EntityNotFoundException("Product with id=$id not found")

        val product = productMapper.toProduct(dto)
        product.id = id
        return productRepository.save(product)
    }
}