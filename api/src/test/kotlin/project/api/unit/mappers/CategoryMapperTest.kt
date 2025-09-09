package project.api.unit.mappers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import project.api.dto.request.business.CategoryDtoRequest
import project.api.entity.Category
import project.api.mapper.business.category.CategoryMapper
import project.api.mapper.business.category.CategoryMapperImpl
import java.util.*


class CategoryMapperTest {
    private lateinit var categoryMapper: CategoryMapper

    @BeforeEach
    fun setUp() {
        categoryMapper = CategoryMapperImpl()
    }

    @Test
    fun toCategoryShouldMapNameCorrectly() {
        val dto = CategoryDtoRequest(name = "Test Category")
        val category: Category = categoryMapper.toCategory(dto)

        assertEquals("Test Category", category.name)
        assertEquals(null, category.id)
    }

    @Test
    fun toCategoryShouldCreateDistinctInstances() {
        val dto1 = CategoryDtoRequest(name = "Category One")
        val dto2 = CategoryDtoRequest(name = "Category Two")

        val cat1 = categoryMapper.toCategory(dto1)
        val cat2 = categoryMapper.toCategory(dto2)

        assertEquals("Category One", cat1.name)
        assertEquals("Category Two", cat2.name)
        assert(cat1 !== cat2)
    }

    @Test
    fun toCategoryShouldHandleNamesWithSpecialCharacters() {
        val dto = CategoryDtoRequest(name = "Category-Name_123")
        val category = categoryMapper.toCategory(dto)

        assertEquals("Category-Name_123", category.name)
    }

    @Test
    fun toDtoShouldMapIdAndNameCorrectly() {
        val category = Category(
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            name = "Electronics"
        )

        val dto = categoryMapper.toDto(category)

        assertEquals(category.id, dto.id)
        assertEquals("Electronics", dto.name)
    }

    @Test
    fun toDtoShouldThrowExceptionWhenIdIsNull() {
        val category = Category(
            id = null,
            name = "Unnamed"
        )

        val exception = org.junit.jupiter.api.assertThrows<IllegalArgumentException> {
            categoryMapper.toDto(category)
        }

        assertEquals("Id is not provided", exception.message)
    }
}
