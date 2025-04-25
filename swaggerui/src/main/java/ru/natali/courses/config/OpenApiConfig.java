package ru.natali.courses.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
//Конфигурация (не обязательная, так как springdoc-auto-configure работает из коробки)
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Student Courses API")
                        .version("1.0")
                        .description("API для управления курсами и студентами")
                        .contact(new Contact()
                                .name("Ваше имя")
                                .email("ваш.email@example.com")));
    }
}
 */