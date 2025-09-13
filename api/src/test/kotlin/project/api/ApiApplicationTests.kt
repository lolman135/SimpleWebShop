package project.api

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import project.api.config.TestSecurityConfig

@SpringBootTest
@Import(TestSecurityConfig::class)
class ApiApplicationTests {

    @Test
    fun contextLoads() {
    }

}
