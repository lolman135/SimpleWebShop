package project.api.mappers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import project.api.dto.ProductDto
import project.api.entity.Feedback
import project.api.mapper.product.ProductMapper
import project.api.mapper.product.ProductMapperImpl
import project.api.repository.feedback.FeedbackRepository
import project.api.repository.product.ProductRepository
import java.util.UUID
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class ProductMapperTest {

    @Mock
    private lateinit var feedbackRepository: FeedbackRepository

    @InjectMocks
    private lateinit var productMapper: ProductMapperImpl

    private lateinit var productDto: ProductDto

    @BeforeEach
    fun setUp(){

        productDto = ProductDto(
            name = "Test product",
            description = "Test description",
            price = 30,
            imageUrl = "https://imgBase:/testImg.com",
            feedbackIds = mutableListOf()
        )
    }

    @Test
    fun testProductMapperShouldCreateProductWithCorrectData(){
        val product = productMapper.mapToProduct(productDto)

        assertEquals("Test product", product.name)
        assertEquals("Test description", product.description)
        assertEquals(30, product.price)
        assertEquals("https://imgBase:/testImg.com", product.imageUrl)
        assertTrue(product.feedbacks.isEmpty())
    }

    @Test
    fun testProductMapperShouldCreateProductWithoutFeedbacks(){
        val product = productMapper.mapToProduct(productDto)
        assertTrue(product.feedbacks.isEmpty())
    }

}
