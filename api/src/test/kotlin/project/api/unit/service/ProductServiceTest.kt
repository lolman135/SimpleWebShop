package project.api.unit.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import project.api.dto.ProductDto
import project.api.entity.Product
import project.api.mapper.product.ProductMapper
import project.api.repository.product.ProductRepository
import project.api.service.business.prodcut.ProductServiceImpl
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class ProductServiceTest {

    @Mock
    private lateinit var productRepository: ProductRepository
    @Mock
    private lateinit var productMapper: ProductMapper

    @InjectMocks
    private lateinit var productService: ProductServiceImpl

    private lateinit var productId: UUID
    private lateinit var productDto: ProductDto
    private lateinit var product: Product

    @BeforeEach
    fun setUp(){
        productId = UUID.randomUUID()
        productDto = ProductDto(
            name = "Test name",
            description = "Test description",
            price = 10,
            imageUrl = "https://imgBase:/testImg.com"
        )
        product = Product(
            id = productId,
            name = "Test name",
            description = "Test description",
            price = 10,
            imageUrl = "https://imgBase:/testImg.com"
        )
    }

    //TODO: implements all tests
}