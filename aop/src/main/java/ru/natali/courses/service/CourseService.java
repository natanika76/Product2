package ru.natali.courses.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.natali.courses.model.Course;
import ru.natali.courses.repository.CourseRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private WebClient webClient;

    public List<Course> getActiveCoursesFromMicroservice() {
        String url = "http://course-management-service:8081/api/courses/active";
        Mono<List<Course>> result = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Course>>() {});
        return result.block(); // Блокирующий вызов для получения результата
    }

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getActiveCourses() {
        return courseRepository.findByActiveTrue();
    }

    public List<Course> getArchivedCourses() {
        return courseRepository.findByActiveFalse();
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id).get();
        course.setActive(false); // Архивируем курс
        courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course courseDetails) {
        // Загружаем существующий курс по ID
        Optional<Course> optionalCourse = courseRepository.findById(id);

        if (optionalCourse.isPresent()) {
            Course existingCourse = optionalCourse.get();

            // Обновляем необходимые поля
            existingCourse.setName(courseDetails.getName());          // Название курса
            existingCourse.setStartDate(courseDetails.getStartDate()); // Дата начала
            existingCourse.setActive(courseDetails.isActive());       // Активность курса

            // Сохраняем изменения
            return courseRepository.save(existingCourse);
        } else {
            throw new RuntimeException("Курс с ID " + id + " не найден");
        }
    }

    // Добавляем метод для получения курса по идентификатору
    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Курс с ID " + courseId + " не найден"));
    }

}