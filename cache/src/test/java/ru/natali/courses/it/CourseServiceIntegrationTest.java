package ru.natali.courses.it;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.natali.courses.model.Course;
import ru.natali.courses.service.CourseService;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Testcontainers
@SpringBootTest
class CourseServiceIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("dbstudents")  // ← Важное изменение
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private CourseService courseService;

    @Test
    void shouldSaveAndRetrieveCourse() {
        Course course = new Course();
        course.setName("Integration Test");
        course.setStartDate(LocalDate.now());  // ← Устанавливаем обязательное поле
        course.setActive(true);

        Course saved = courseService.createCourse(course);
        Course found = courseService.getCourseById(saved.getId());

        assertThat(found.getName(), is("Integration Test"));
        assertThat(found.getStartDate(), is(course.getStartDate()));
    }

    @Test
    void testCourseName() {
        Course course = new Course();
        course.setName("Spring Boot");

        assertThat(course.getName(),
                allOf(
                        is("Spring Boot"),
                        containsString("Boot"),
                        not(emptyString()),
                        notNullValue()
                ));
    }

}
