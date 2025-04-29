package ru.natali.courses.controller;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@Testcontainers // Включаем поддержку Testcontainers
public class LoginControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    // Запускаем контейнер PostgreSQL
    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("dbstudents")  // ← Важное изменение
            .withUsername("testuser")
            .withPassword("testpass");
    // Динамически переопределяем свойства подключения к БД
    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    void setup() {
        // Настраиваем RESTAssured на работу с MockMvc
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    void testLoginPage() {
        given()
                .when()
                .get("/login")
                .then()
                .statusCode(200)
                .body(containsString("login")); // Проверяем, что возвращается страница login
    }
}
