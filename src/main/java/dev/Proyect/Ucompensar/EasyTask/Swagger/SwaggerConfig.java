package dev.Proyect.Ucompensar.EasyTask.Swagger;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    // Define a bean for the OpenAPI configuration
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")  // The name of the group
                .pathsToMatch("/api/**")  // Paths to include in this group
                .build();
    }

    // Add more grouped APIs if needed (private, admin, etc.)
}
