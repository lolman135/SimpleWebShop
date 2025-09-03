package project.api.unit.mappers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import project.api.dto.business.CategoryDto
import project.api.entity.Category
import project.api.mapper.business.category.CategoryMapper
import project.api.mapper.business.category.CategoryMapperImpl

@SpringBootTest
class CategoryMapperTest {

    @Autowired
    private lateinit var categoryMapper: CategoryMapper

    @BeforeEach
    fun setUp() {
        categoryMapper = CategoryMapperImpl()
    }

    @Test
    fun toCategoryShouldMapNameCorrectly() {
        val dto = CategoryDto(name = "Test Category")
        val category: Category = categoryMapper.toCategory(dto)

        assertEquals("Test Category", category.name)
        assertEquals(null, category.id)
    }

    @Test
    fun toCategoryShouldCreateDistinctInstances() {
        val dto1 = CategoryDto(name = "Category One")
        val dto2 = CategoryDto(name = "Category Two")

        val cat1 = categoryMapper.toCategory(dto1)
        val cat2 = categoryMapper.toCategory(dto2)

        assertEquals("Category One", cat1.name)
        assertEquals("Category Two", cat2.name)
        assert(cat1 !== cat2)
    }

    @Test
    fun toCategoryShouldHandleNamesWithSpecialCharacters() {
        val dto = CategoryDto(name = "Category-Name_123")
        val category = categoryMapper.toCategory(dto)

        assertEquals("Category-Name_123", category.name)
    }
}
