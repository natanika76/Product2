package ru.natali.courses.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.natali.courses.dto.*;
import ru.natali.courses.model.Course;
import ru.natali.courses.model.Role;
import ru.natali.courses.model.Student;
import ru.natali.courses.service.CourseService;
import ru.natali.courses.service.StudentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//GET http://localhost:8080/swagger-ui/index.html
@RestController
@RequestMapping("/api/courses")
@Tag(name = "Управление курсами", description = "API для управления курсами и студентами")
public class CourseController {

    private final PasswordEncoder passwordEncoder;
    private final CourseService courseService;
    private final StudentService studentService;

    public CourseController(PasswordEncoder passwordEncoder,
                            CourseService courseService,
                            StudentService studentService) {
        this.passwordEncoder = passwordEncoder;
        this.courseService = courseService;
        this.studentService = studentService;
    }

    //Методы для работы с курсами
    //GET http://localhost:8080/api/courses
    @Operation(summary = "Получить все курсы", description = "Возвращает список всех курсов")
    @ApiResponse(responseCode = "200", description = "Список курсов успешно получен")
    @GetMapping
    public List<CourseDTO> getAllCourses() {
        return courseService.getAllCourses().stream()
                .map(course -> new CourseDTO(
                        course.getId(),
                        course.getName(),
                        course.getStartDate(),
                        course.isActive(),
                        course.isArchived(),
                        course.getStudents().size()
                ))
                .collect(Collectors.toList());
    }

    //GET http://localhost:8080/api/courses/active
    @Operation(summary = "Получить активные курсы", description = "Возвращает список активных курсов")
    @ApiResponse(responseCode = "200", description = "Список активных курсов успешно получен")
    @GetMapping("/active")
    public List<CourseBasicDTO> getActiveCourses() {
        return courseService.getActiveCourses();
    }

    //GET http://localhost:8080/api/courses/archived
    @Operation(summary = "Получить архивные курсы", description = "Возвращает список архивных курсов")
    @ApiResponse(responseCode = "200", description = "Список архивных курсов успешно получен")
    @GetMapping("/archived")
    public List<CourseBasicDTO> getArchivedCourses() {
        return courseService.getArchivedCourses().stream()
                .map(CourseBasicDTO::new)
                .collect(Collectors.toList());
    }

    /*
    POST http://localhost:8080/api/courses
    {
    "name": "Новый курс",
    "startDate": "2024-01-01",
    "active": true,
    "archived": false
    }*/
    @Operation(summary = "Создать новый курс", description = "Создает новый курс с предоставленными данными")
    @ApiResponse(responseCode = "200", description = "Курс успешно создан")
    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    //DELETE http://localhost:8080/api/courses/8
    // НЕ ЗАБЫВАТЬ, что удаление МЯГКОЕ!!! атрибут is_active становится false
    @Operation(summary = "Удалить курс", description = "Удаляет курс по его идентификатору")
    @ApiResponse(responseCode = "200", description = "Курс успешно удален")
    @ApiResponse(responseCode = "404", description = "Курс не найден")
    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

    /*
   PUT http://localhost:8080/api/courses/2
   {
   "name": "New Updated Course",
    "startDate": "2023-11-01",
   "active": false
   }
   //именно так - без archived
   */
    @Operation(summary = "Обновить курс", description = "Обновляет существующий курс с предоставленными данными")
    @ApiResponse(responseCode = "200", description = "Курс успешно обновлен")
    @ApiResponse(responseCode = "404", description = "Курс не найден")
    /*@PutMapping("/{id}")
    public ResponseEntity<CourseBasicDTO> updateCourse(
            @PathVariable Long id,
            @RequestBody CourseBasicDTO courseDetails
    ) {
        Course updatedCourse = courseService.updateCourse(id, courseDetails);
        CourseBasicDTO responseDto = new CourseBasicDTO(updatedCourse);
        return ResponseEntity.ok(responseDto);
    }*/
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(
            @PathVariable Long id,
            @RequestBody CourseBasicDTO courseDetails
    ) {
        try {
            Course updatedCourse = courseService.updateCourse(id, courseDetails);
            CourseBasicDTO responseDto = new CourseBasicDTO(updatedCourse);
            return ResponseEntity.ok(responseDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Методы для работы со студентами
    //GET http://localhost:8080/api/courses/3/students
    @Operation(summary = "Получить студентов курса", description = "Возвращает всех студентов, записанных на указанный курс")
    @ApiResponse(responseCode = "200", description = "Список студентов успешно получен")
    @ApiResponse(responseCode = "404", description = "Курс не найден")
    @GetMapping("/{courseId}/students")
    public List<Student> getStudentsByCourse(@PathVariable Long courseId) {
        Course course = courseService.getCourseById(courseId);
        return studentService.getStudentsByCourse(course);
    }

    /*
    POST http://localhost:8080/api/courses/students
    Content-Type: application/json
    {
        "firstName": "Федор",
        "lastName": "Шаляпин",
        "email": "fedorshalyapin@example.com",
        "username": "shalyapa",
        "password": "1111"
    }
    */
    @Operation(summary = "Создать студента", description = "Создает нового студента с предоставленными данными")
    @ApiResponse(responseCode = "200", description = "Студент успешно создан")
    @PostMapping("/students")
    public Student createStudent(@RequestBody CreateStudentDTO studentDTO) {
        Student student = new Student();
        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        student.setEmail(studentDTO.getEmail());
        student.setUsername(studentDTO.getUsername());
        student.setPassword(passwordEncoder.encode(studentDTO.getPassword()));
        student.setRole(Role.USER);

        return studentService.createStudent(student);
    }

    /*PUT http://localhost:8080/api/courses/students/24
    {
    "firstName": "Иван",
    "lastName": "Иванов",
    "email": "new_email@example.com",
    "username": "ivi",
    "password": "1111"
}
    */
    @Operation(summary = "Обновить данные студента", description = "Обновляет данные существующего студента")
    @ApiResponse(responseCode = "200", description = "Данные студента успешно обновлены")
    @ApiResponse(responseCode = "404", description = "Студент не найден")
    @PutMapping("/students/{studentId}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long studentId,
            @RequestBody Student studentDetails) {
        Student updatedStudent = studentService.updateStudent(studentId, studentDetails);
        return ResponseEntity.ok(updatedStudent);
    }

    // Дополнительные методы
    //GET http://localhost:8080/api/courses/5/students-by-last-name-starts-with/M
    @Operation(summary = "Получить студентов по первой букве фамилии",
            description = "Возвращает студентов курса, чьи фамилии начинаются с указанной буквы")
    @ApiResponse(responseCode = "200", description = "Отфильтрованный список студентов успешно получен")
    @ApiResponse(responseCode = "404", description = "Курс не найден")
    @GetMapping("/{courseId}/students-by-last-name-starts-with/{letter}")
    public List<Student> getStudentsByCourseAndLastNameStartsWith(
            @PathVariable Long courseId,
            @PathVariable char letter
    ) {
        Course course = courseService.getCourseById(courseId);
        return studentService.findStudentsByCourseAndLastNameStartsWith(course, letter);
    }

    //GET http://localhost:8080/api/courses/statistics
    @Operation(summary = "Получить статистику по курсам",
            description = "Возвращает статистику по курсам (количество студентов, даты, активность)")
    @ApiResponse(responseCode = "200", description = "Статистика по курсам успешно получена")
    @GetMapping("/statistics")
    public List<Map<String, Object>> getCourseStatistics() {
        return courseService.getCourseStatisticsReport();
    }

    //GET http://localhost:8080/api/courses/empty-assignments
    @Operation(summary = "Получить отчет по пустым назначениям",
            description = "Возвращает отчет по курсам без назначенных студентов")
    @ApiResponse(responseCode = "200", description = "Отчет по пустым назначениям успешно получен")
    @GetMapping("/empty-assignments")
    public EmptyAssignmentReportDTO getEmptyAssignmentsReport() {
        return courseService.getEmptyAssignmentsReport(); // Вернуть новый DTO
    }

    // GET http://localhost:8080/api/courses/by-month
    @Operation(summary = "Получить распределение курсов по месяцам",
            description = "Возвращает распределение курсов по месяцам создания")
    @ApiResponse(responseCode = "200", description = "Распределение по месяцам успешно получено")
    @GetMapping("/by-month")
    public Map<String, Long> getCoursesByMonth() {
        return courseService.getCoursesByMonthReport();
    }

    // GET http://localhost:8080/api/courses/full-report
    @Operation(summary = "Получить полный отчет",
            description = "Возвращает комплексный отчет, включающий всю доступную статистику")
    @ApiResponse(responseCode = "200", description = "Полный отчет успешно получен")
    @GetMapping("/full-report")
    public Map<String, Object> getFullReport() {
        Map<String, Object> report = new HashMap<>();
        report.put("courseStatistics", courseService.getCourseStatisticsReport());
        report.put("activeCoursesCount", courseService.getActiveCourses().size());
        report.put("archivedCoursesCount", courseService.getArchivedCourses().size());
        report.put("coursesByMonth", courseService.getCoursesByMonthReport());
        report.put("emptyAssignments", courseService.getEmptyAssignmentsReport());
        return report;
    }

    // GET http://localhost:8080/api/courses/4/last-name-distribution
    @Operation(summary = "Получить распределение по первой букве фамилии",
            description = "Возвращает распределение студентов курса по первой букве фамилии")
    @ApiResponse(responseCode = "200", description = "Распределение по фамилиям успешно получено")
    @ApiResponse(responseCode = "404", description = "Курс не найден")
    @GetMapping("/{courseId}/last-name-distribution")
    public Map<Character, Long> getLastNameInitialDistribution(@PathVariable Long courseId) {
        return courseService.getLastNameInitialDistribution(courseId);
    }

    // // GET http://localhost:8080/api/courses/students/2/courses
    @Operation(summary = "Получить курсы студента",
            description = "Возвращает все курсы, на которые записан указанный студент")
    @ApiResponse(responseCode = "200", description = "Курсы студента успешно получены")
    @ApiResponse(responseCode = "404", description = "Студент не найден")
    @GetMapping("/students/{studentId}/courses")
    public List<SimpleCourseDTO> getCoursesForStudent(@PathVariable Long studentId) {
        return courseService.getCoursesForStudent(studentId);
    }
}