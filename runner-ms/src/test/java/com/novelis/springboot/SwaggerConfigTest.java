package com.novelis.springboot;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SwaggerConfigTest {

    @Autowired
    private OpenAPI openAPI;

    @Test
    void openApiBean_shouldBeCreated() {
        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getInfo().getTitle())
                .isEqualTo("Spring Boot 3.0.0 API Documentation");
    }
}
