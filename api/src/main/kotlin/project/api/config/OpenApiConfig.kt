package project.api.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAip(): OpenAPI =
        OpenAPI()
            .info(Info()
                .title("Web shop Api")
                .version("1.0.0")
                .description("REST API for system")
                .contact(Contact()
                    .name("Kyrylo Kusovskyi")
                    .email("kkirilll2006@gmail.com")
                )
            )
}